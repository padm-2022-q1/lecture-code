package br.edu.ufabc.cronometroviewmodellivedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

class MainViewModel : ViewModel() {
    enum class State {INITIAL, STARTED, STOPPED}

    val timeElapsed: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0L)
    }
    val state : MutableLiveData<State> by lazy {
        MutableLiveData<State>(State.INITIAL)
    }

    init {
        runTimer()
    }

    private fun runTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                if (isTimerRunning())
                    timeElapsed.value?.let {
                        timeElapsed.value = it + 1
                    }
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun isTimerRunning() = state.value == State.STARTED
}