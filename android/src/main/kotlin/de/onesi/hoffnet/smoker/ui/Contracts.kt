package de.onesi.hoffnet.smoker.ui

import android.os.Bundle

interface BaseView {

}

interface BasePresenter<V : BaseView> {
    fun bind(view: V?, state: Bundle?)
    fun start()
    fun saveState(bundle: Bundle)
    fun destroy()
}