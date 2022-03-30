package br.edu.ufabc.cronometroviewmodellivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import br.edu.ufabc.cronometroviewmodellivedata.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindEvents()
        registerObservers()
    }

    private fun updateTime(timeElapsed: Long) {
        val hours = timeElapsed / 3600
        val minutes = timeElapsed % 3600 / 60
        val seconds = timeElapsed % 60

        binding.textviewTimeElapsed.text = getString(R.string.time_format, hours, minutes, seconds)
    }

    private fun formatStart() {
        binding.buttonStartStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.purple_500))
        binding.buttonStartStop.text = getString(R.string.button_start_stop_default)
    }

    private fun formatStop() {
        binding.buttonStartStop.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.red))
        binding.buttonStartStop.text = getString(R.string.button_stop)
    }

    private fun registerObservers() {
        viewModel.state.observe(this) {
            it?.let { state ->
                when (state) {
                    MainViewModel.State.STARTED -> formatStop()
                    MainViewModel.State.STOPPED -> formatStart()
                    MainViewModel.State.INITIAL -> {
                        viewModel.timeElapsed.value = 0
                        formatStart()
                    }
                }
            }
        }

        viewModel.timeElapsed.observe(this) {
            it?.let { timeElapsed -> updateTime(timeElapsed)}
        }
    }

    private fun bindEvents() {
        binding.buttonStartStop.setOnClickListener {
             viewModel.state.value = if (viewModel.isTimerRunning()) {
                 MainViewModel.State.STOPPED
             } else {
                 MainViewModel.State.STARTED
             }
        }
        binding.buttonReset.setOnClickListener {
            viewModel.state.value = MainViewModel.State.INITIAL
        }
    }


}