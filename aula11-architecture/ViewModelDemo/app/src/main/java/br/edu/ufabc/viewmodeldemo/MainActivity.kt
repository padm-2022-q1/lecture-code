package br.edu.ufabc.viewmodeldemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // obtain an ViewModel instance for the current Activity
        val mainViewModel: MainViewModel by viewModels()
//        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        Log.d(App.LOGTAG, mainViewModel.mainValue)

        Log.d(App.LOGTAG, "${this::class.java.canonicalName} created")
    }

    override fun onStart() {
        super.onStart()

        findViewById<Button>(R.id.button_open).setOnClickListener {
            startActivity(Intent(this, SecondaryActivity::class.java))
        }

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