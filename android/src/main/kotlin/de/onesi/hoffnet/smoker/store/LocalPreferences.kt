package de.onesi.hoffnet.smoker.store

import android.content.SharedPreferences
import javax.inject.Inject

interface LocalPreferences {
    var lastUsedAddress: String?
}

class DefaultLocalPreferences @Inject constructor(private val prefs: SharedPreferences) : LocalPreferences {

    override var lastUsedAddress: String?
        get() = prefs.getString("last-used-address", null)
        set(value) {
            prefs.edit().putString("last-used-address", value).apply()
        }
}