package br.edu.ufabc.padm.traceactivitylifecycle

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MessageRenderActivity : AppCompatActivity() {

    companion object {
        private val LOGTAG = "LIFECYCLETEST on ${MessageRenderActivity::class.qualifiedName} : "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_render)

        // retrieve the intent
        val intent = intent
        // get the data from the intent
        val message = intent.getStringExtra(NameInputActivity.NAME_EXTRA_KEY)
        // retrieve the target text field
        val renderResult = findViewById<TextView>(R.id.render_result)

        // Build a message and assign to the target text field
        renderResult.text = getString(R.string.result_message, message)

        Log.d(LOGTAG, "created")
        Log.d(LOGTAG, "task id is " + this.taskId)
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
