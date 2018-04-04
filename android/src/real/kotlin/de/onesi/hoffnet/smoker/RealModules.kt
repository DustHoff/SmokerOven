package de.onesi.hoffnet.smoker

import dagger.Module
import dagger.Provides
import de.onesi.hoffnet.smoker.server.SmokerServer
import de.onesi.hoffnet.smoker.server.SmokerServerFactory
import retrofit2.Retrofit

class RealSmokerServerFactory : SmokerServerFactory() {

    override fun onCreate(retrofit: Retrofit): SmokerServer = retrofit.create(SmokerServer::class.java)

}

@Module
class FlavorModule {

    @Provides
    fun smokerServerFactory(): SmokerServerFactory = RealSmokerServerFactory()

}