package de.onesi.hoffnet.smoker.store

import android.os.Parcel
import android.os.Parcelable
import de.onesi.hoffnet.states.OvenState
import de.onesi.hoffnet.web.data.State
import java.util.*

class StateParcelable(val state: State?) : Parcelable {

    constructor(p: Parcel) : this(CREATOR.extractState(p))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeString(state?.ovenState?.name)
            writeLong(state?.timestamp?.time ?: 0)
            writeString(state?.message)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StateParcelable> {
        override fun createFromParcel(parcel: Parcel): StateParcelable {
            return StateParcelable(parcel)
        }

        override fun newArray(size: Int): Array<StateParcelable?> {
            return arrayOfNulls(size)
        }

        private fun extractState(parcel: Parcel): State? {
            return parcel.readString()?.let {
                State(OvenState.valueOf(it), Date(parcel.readLong()), parcel.readString())
            }
        }
    }
}