package de.onesi.hoffnet.smoker.server

import android.net.Uri
import de.onesi.hoffnet.events.OvenEvent
import de.onesi.hoffnet.web.data.Configuration
import de.onesi.hoffnet.web.data.State
import de.onesi.hoffnet.web.data.Temperature
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SmokerServer {

    @GET("/state")
    fun state(): Flowable<State>

    @POST("/state")
    fun event(@Body event: OvenEvent): Flowable<Response<Void>>

    @POST("/configure")
    fun configure(@Body config: Configuration): Flowable<Configuration>

    @GET("/configure")
    fun configuration(): Flowable<Configuration>

    @GET("/temperature")
    fun temperature(): Flowable<Temperature>
}

abstract class SmokerServerFactory {

    fun create(uri: Uri): SmokerServer {
        return Retrofit.Builder()
                .baseUrl(uri.toString())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create())
                .also { onConfigure(it) }
                .build()
                .let { onCreate(it) }
    }

    protected open fun onConfigure(builder: Retrofit.Builder) { }

    protected abstract fun onCreate(retrofit: Retrofit): SmokerServer
}