package de.onesi.hoffnet.smoker

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.android.DaggerService
import de.onesi.hoffnet.smoker.server.SmokerServer
import de.onesi.hoffnet.smoker.server.SmokerServerFactory
import javax.inject.Inject

class SmokerService : DaggerService() {

    @Inject
    lateinit var serverFactory: SmokerServerFactory

    override fun onBind(intent: Intent): IBinder? {
        return serverFactory.create(intent.data).let { smokerServer ->
            SmokerServiceBinder(smokerServer)
        }
    }
}

class SmokerServiceBinder(val service: SmokerServer) : Binder() {

}