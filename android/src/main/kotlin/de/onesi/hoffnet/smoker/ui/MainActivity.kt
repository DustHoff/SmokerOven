package de.onesi.hoffnet.smoker.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection
import de.onesi.hoffnet.smoker.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        presenter.bind(this, savedInstanceState)

        textInputAddress.editText?.apply {
            setOnEditorActionListener { tv, actionId, _ ->
                presenter.onAddressEditorAction(actionId).also {
                    if (it) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(tv.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    }
                }
            }
        }

        btnStart.setOnClickListener { presenter.onStartClicked() }
        btnStop.setOnClickListener { presenter.onStopClicked() }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun enableView(config: Boolean?, start: Boolean?, stop: Boolean?) {
        config?.let {
            editRoomTemp.isEnabled = it
            editObjectTemp.isEnabled = it
            editTolerance.isEnabled = it
        }

        start?.let { btnStart.isEnabled = it }
        stop?.let { btnStop.isEnabled = it }
    }

    override var serverAddress: CharSequence?
        get() = textInputAddress.editText?.text
        set(value) {
            textInputAddress.editText?.setText(value)
        }

    override var serverAddressInputError: CharSequence?
        get() = editAddress.error
        set(value) {
            editAddress.error = value
        }

    override var currentState: CharSequence?
        get() = textState.text
        set(value) {
            textState.text = value
        }

    override var currentObjectTemp: CharSequence?
        get() = textCurrentObjectTemp.text
        set(value) {
            textCurrentObjectTemp.text = value
        }

    override var currentRoomTemp: CharSequence?
        get() = textCurrentRoomTemp.text
        set(value) {
            textCurrentRoomTemp.text = value
        }

    override var configRoomTemp: CharSequence?
        get() = editRoomTemp.text
        set(value) {
            editRoomTemp.setText(value)
        }

    override var configObjectTemp: CharSequence?
        get() = editObjectTemp.text
        set(value) {
            editObjectTemp.setText(value)
        }

    override var configTolerance: CharSequence?
        get() = editTolerance.text
        set(value) {
            editTolerance.setText(value)
        }

    override var activeLoading: Boolean
        get() = progressBar.visibility != View.GONE
        set(value) {
            if (value) {
                progressBar.show()
            } else {
                progressBar.hide()
            }

        }
}
