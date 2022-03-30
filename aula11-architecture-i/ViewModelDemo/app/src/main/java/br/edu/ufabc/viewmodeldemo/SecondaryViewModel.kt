package br.edu.ufabc.viewmodeldemo

import android.util.Log
import androidx.lifecycle.ViewModel

class SecondaryViewModel: ViewModel() {
    val value = "I am the secondary view model"

    init {
        Log.d(App.LOGTAG, "%s created".format(javaClass.canonicalName))
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(App.LOGTAG, "%s cleared".format(javaClass.canonicalName))
    }
}