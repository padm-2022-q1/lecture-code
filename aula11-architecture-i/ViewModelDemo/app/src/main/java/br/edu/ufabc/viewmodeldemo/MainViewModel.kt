package br.edu.ufabc.viewmodeldemo

import android.util.Log

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val mainValue = "I am the main view model"
    init {
        Log.d(App.LOGTAG, "%s created".format(javaClass.canonicalName))
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(App.LOGTAG, "%s cleared".format(javaClass.canonicalName))
    }
}
