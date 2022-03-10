package br.edu.ufabc.padm.traceactivitylifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class NameInputActivity : AppCompatActivity() {

    companion object {
        private val LOGTAG = "LIFECYCLETEST on ${NameInputActivity::class.qualifiedName} :"
        val NAME_EXTRA_KEY = "nameExtra"
    }

    private lateinit var nameInputText: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)
        title = getString(R.string.name_input_activity_title)

        bindComponents();

        Log.d(LOGTAG, "created")
        Log.d(LOGTAG, " task id is " + this.taskId)
    }

    fun bindComponents() {
        nameInputText = findViewById(R.id.name_input)
        button = findViewById(R.id.button_ok)

        button.setOnClickListener {
            // Create an intent for NameInputActivity
            val intent = Intent(this, MessageRenderActivity::class.java)
            // Retrieve the typed text
            val inputText = nameInputText.text.toString()

            // put additional data in the intent
            intent.putExtra(NAME_EXTRA_KEY, inputText)

            // command the intent to start the target activity
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(LOGTAG, "started")
    }

    override fun onResume() {
        super.onResume()

        Log.d(LOGTAG, "resumed")
    }

    override fun onPause() {
        super.onPause()

        Log.d(LOGTAG, "paused")
    }

    override fun onStop() {
        super.onStop()

        Log.d(LOGTAG, "stopped")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(LOGTAG, "destroyed")
    }

    override fun onRestart() {
        super.onRestart()

        Log.d(LOGTAG, "restarted")
    }
}
