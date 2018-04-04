package de.onesi.hoffnet.smoker.server

import android.util.Log
import de.onesi.hoffnet.events.OvenEvent
import de.onesi.hoffnet.web.data.Configuration
import de.onesi.hoffnet.web.data.State
import de.onesi.hoffnet.web.data.Temperature
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


private val TAG = ServerController::class.java.simpleName

class ServerController(private val server: SmokerServer) {

    private val processingPublisher = PublishSubject.create<Boolean>()

    val loadingActive: Flowable<Boolean> = processingPublisher.toFlowable(BackpressureStrategy.LATEST)

    fun pollState(intervalSecs: Long = 10,
                  scheduler: Scheduler = mainThread()): Flowable<State> {

        return Flowable
                .interval(0, intervalSecs, TimeUnit.SECONDS)
                .doOnEach { processingPublisher.onNext(true) }
                .flatMap { _ -> server.state() }
                .doOnEach {
                    processingPublisher.onNext(false)
                    Log.d(TAG, "polled state: $it")
                }
                .observeOn(scheduler)
    }

    fun state(scheduler: Scheduler = mainThread()): Flowable<State> = server.state()
            .doOnRequest { processingPublisher.onNext(true) }
            .doOnEach {
                processingPublisher.onNext(false)
                Log.d(TAG, "got single state: $it")
            }
            .observeOn(scheduler)

    fun pollTemperature(intervalSecs: Long = 10,
                        scheduler: Scheduler = mainThread()): Flowable<Temperature> {

        return Flowable
                .interval(0, intervalSecs, TimeUnit.SECONDS)
                .doOnEach { processingPublisher.onNext(true) }
                .flatMap { _ -> server.temperature() }
                .doOnEach {
                    processingPublisher.onNext(false)
                    Log.d(TAG, "polled temperature: $it")
                }
                .observeOn(scheduler)
    }

    fun temperature(scheduler: Scheduler = mainThread()) = server.state()
            .doOnRequest { processingPublisher.onNext(true) }
            .doOnEach {
                processingPublisher.onNext(false)
                Log.d(TAG, "got single temperature: $it")
            }
            .observeOn(scheduler)

    fun configuration(config: Configuration? = null,
                      scheduler: Scheduler = mainThread()): Flowable<Configuration> {
        val observable = config?.let { server.configure(it) } ?: server.configuration()

        observable
                .doOnRequest { processingPublisher.onNext(true) }
                .doOnEach {
                    processingPublisher.onNext(false)
                    Log.d(TAG, "got config: $it")
                }

        return observable.observeOn(scheduler)
    }

    fun sendEvent(event: OvenEvent, scheduler: Scheduler = mainThread()): Flowable<Boolean> {
        return server.event(event)
                .doOnRequest { processingPublisher.onNext(true) }
                .flatMap { Flowable.just(it.isSuccessful) }
                .doOnEach {
                    processingPublisher.onNext(false)
                    Log.d(TAG, "sent event: $event")
                }
                .observeOn(scheduler)
    }
}