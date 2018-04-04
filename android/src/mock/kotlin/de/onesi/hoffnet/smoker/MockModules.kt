package de.onesi.hoffnet.smoker

import dagger.Module
import dagger.Provides
import de.onesi.hoffnet.smoker.server.SmokerServerFactory

@Module
class FlavorModule {

    @Provides
    fun smokerServerFactory(): SmokerServerFactory = MockSmokerServerFactory()

}
