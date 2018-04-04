package de.onesi.hoffnet.smoker

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import de.onesi.hoffnet.smoker.store.DefaultLocalPreferences
import de.onesi.hoffnet.smoker.store.LocalPreferences
import de.onesi.hoffnet.smoker.ui.MainActivityModule

@Subcomponent
interface SmokerServiceSubcomponent : AndroidInjector<SmokerService> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SmokerService>()
}

@Module(subcomponents = [SmokerServiceSubcomponent::class])
abstract class SmokerServiceModule {

    @Binds
    @IntoMap
    @ServiceKey(SmokerService::class)
    abstract fun bindInjectorFactory(builder: SmokerServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>

}

@Module
abstract class MainModule {

    @Binds
    abstract fun provideLocalPreferences(prefs: DefaultLocalPreferences): LocalPreferences

}

@Module
class SystemModule(val context: Context) {

    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }

}

@Component(modules = [
    FlavorModule::class,
    SystemModule::class,
    MainModule::class,
    MainActivityModule::class,
    SmokerServiceModule::class
])
interface ApplicationComponent : AndroidInjector<SmokerApplication>