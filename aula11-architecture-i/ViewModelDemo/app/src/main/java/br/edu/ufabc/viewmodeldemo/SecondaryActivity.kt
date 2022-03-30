package br.edu.ufabc.viewmodeldemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels

class SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        val secondaryViewModel: SecondaryViewModel by viewModels()

        Log.d(App.LOGTAG, secondaryViewModel.value)

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} created")
    }

    override fun onStart() {
        super.onStart()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} started")
    }

    override fun onResume() {
        super.onResume()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} resumed")
    }

    override fun onPause() {
        super.onPause()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} paused")
    }

    override fun onStop() {
        super.onStop()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} stopped")
    }

    override fun onRestart() {
        super.onRestart()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} restarted")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} destroyed")
    }
}