package br.edu.ufabc.fragmentsdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class StaticFragmentActivity : AppCompatActivity() {

    companion object {
        private val LOGTAG = StaticFragmentActivity::class.java.getSimpleName() + " TRACEFRAGMENTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_fragment)
        Log.d(LOGTAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOGTAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOGTAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOGTAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOGTAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOGTAG, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(LOGTAG, "onRestart")
    }
}
