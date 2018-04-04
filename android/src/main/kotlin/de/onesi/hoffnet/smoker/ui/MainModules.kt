package de.onesi.hoffnet.smoker.ui

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Subcomponent
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}

@Module(subcomponents = [MainActivitySubcomponent::class])
class MainActivityModule {

    @Provides
    @IntoMap
    @ActivityKey(MainActivity::class)
    fun bindInjectorFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity> = builder

    @Provides
    fun bindMainPresenter(presenter: DefaultMainPresenter): MainPresenter = presenter

}