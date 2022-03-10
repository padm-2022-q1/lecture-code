package br.edu.ufabc.cronometro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import br.edu.ufabc.cronometro.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timeElapsed = 0L
    private enum class State : Serializable {INITIAL, STARTED, STOPPED}
    private lateinit var state: State

    companion object {
        private const val timeElapsedKey = "timeElapsed"
        private const val stateKey = "state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setState(State.INITIAL)
        runTimer()
        bindEvents()
    }

    private fun updateTime() {
        val hours = timeElapsed / 3600
        val minutes = timeElapsed % 3600 / 60
        val seconds = timeElapsed % 60

        binding.textviewTimeElapsed.text = getString(R.string.time_format, hours, minutes, seconds)
    }

    private fun runTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                if (isRunning())
                    timeElapsed++
                updateTime()
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun formatStart() {
        binding.buttonStartStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_500))
        binding.buttonStartStop.text = getString(R.string.button_start_stop_default)
    }

    private fun formatStop() {
        binding.buttonStartStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.red))
        binding.buttonStartStop.text = getString(R.string.button_stop)
    }

    private fun setState(state: State) {
        when (state) {
            State.STARTED -> {
                formatStop()
            }
            State.STOPPED -> {
                formatStart()
            }
            State.INITIAL -> {
                timeElapsed = 0
                formatStart()
            }
        }
        this.state = state
        updateTime()
    }

    private fun isRunning() = state == State.STARTED

    private fun bindEvents() {
        binding.buttonStartStop.setOnClickListener { setState(if (isRunning()) State.STOPPED else State.STARTED) }
        binding.buttonReset.setOnClickListener { setState(State.INITIAL) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(timeElapsedKey, timeElapsed)
        outState.putSerializable(stateKey, state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        timeElapsed = savedInstanceState.getLong(timeElapsedKey)
        setState(savedInstanceState.getSerializable(stateKey) as State)
    }
}