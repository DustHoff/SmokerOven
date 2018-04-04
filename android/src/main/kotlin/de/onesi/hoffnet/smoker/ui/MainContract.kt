package de.onesi.hoffnet.smoker.ui

interface MainView : BaseView {

    var serverAddress: CharSequence?
    var serverAddressInputError: CharSequence?

    var currentState: CharSequence?
    var currentObjectTemp: CharSequence?
    var currentRoomTemp: CharSequence?

    var configRoomTemp : CharSequence?
    var configObjectTemp : CharSequence?
    var configTolerance : CharSequence?

    fun enableView(config: Boolean? = null,
                   start: Boolean? = null,
                   stop: Boolean? = null)

    var activeLoading : Boolean
}

interface MainPresenter : BasePresenter<MainView> {

    fun onAddressEditorAction(actionId: Int): Boolean
    fun onStartClicked()
    fun onStopClicked()

}