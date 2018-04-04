package de.onesi.hoffnet.smoker

import de.onesi.hoffnet.smoker.server.SmokerServer
import de.onesi.hoffnet.smoker.server.SmokerServerFactory
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit

class MockSmokerServerFactory : SmokerServerFactory() {

    override fun onCreate(retrofit: Retrofit): SmokerServer {
        val mockRetrofit = MockRetrofit.Builder(retrofit).build()
        val behaviorDelegate = mockRetrofit.create(SmokerServer::class.java)
        return MockSmokerServer(behaviorDelegate)
    }
}