package de.onesi.hoffnet.smoker

import de.onesi.hoffnet.events.OvenEvent
import de.onesi.hoffnet.smoker.server.SmokerServer
import de.onesi.hoffnet.states.OvenState
import de.onesi.hoffnet.web.data.Configuration
import de.onesi.hoffnet.web.data.State
import de.onesi.hoffnet.web.data.Temperature
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.mock.BehaviorDelegate
import java.util.*

private val TAG = MockSmokerServer::class.java.simpleName

class MockSmokerServer(val behaviorDelegate: BehaviorDelegate<SmokerServer>) : SmokerServer {

    val rand = Random()
    var ovenState = OvenState.INITIALIZE

    var config: Configuration = Configuration().apply {
        objectTemperature = 1.0
        roomTemperature = 1.0
        temperatureTolerance = 1.0
        startDate = Date()
    }

    override fun state(): Flowable<State> {
        val currentState = ovenState
        val state = State(currentState)
        ovenState = currentState.next.shuffled().first()
        return behaviorDelegate.returningResponse(state).state()
    }

    override fun event(event: OvenEvent): Flowable<Response<Void>> {
        return behaviorDelegate.returningResponse(Response.success(null)).event(event)
    }

    override fun configure(config: Configuration): Flowable<Configuration> {
        this.config = config
        return behaviorDelegate.returningResponse(config).configure(config)
    }

    override fun configuration(): Flowable<Configuration> {
        return behaviorDelegate.returningResponse(config).configuration()
    }

    override fun temperature(): Flowable<Temperature> {
        val temp = Temperature(rand.nextDouble() * 30, rand.nextDouble() * 30)
        return behaviorDelegate.returningResponse(temp).temperature()
    }

}