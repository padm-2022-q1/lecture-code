package br.edu.ufabc.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction


class DynamicFragmentsActivity : AppCompatActivity() {

    companion object {
        private const val logTag = "APP-FRAG"
    }

    private lateinit var topTrigger: Button
    private lateinit var bottomTrigger: Button
    private lateinit var fragmentTop: StaticFragmentTop
    private lateinit var fragmentBottom: StaticFragmentBottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_fragments)
        init()
        bindHandlers()
        Log.d(logTag, "onCreate")
    }

    private fun init() {
        topTrigger = findViewById(R.id.top_trigger)
        bottomTrigger = findViewById(R.id.bottom_trigger)
        fragmentTop = StaticFragmentTop()
        fragmentBottom = StaticFragmentBottom()
    }

    private fun bindHandlers() {
        topTrigger.setOnClickListener {
            Log.d(logTag, "Top button clicked")
            val transaction = supportFragmentManager.beginTransaction()

            transaction.add(R.id.top_container, fragmentTop)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.addToBackStack(null)
            transaction.commit()
            topTrigger.isEnabled = false
        }
        bottomTrigger.setOnClickListener {
            Log.d(logTag, "Bottom button clicked")
            val transaction = supportFragmentManager.beginTransaction()

            transaction.add(R.id.bottom_container, fragmentBottom)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.addToBackStack(null)
            transaction.commit()
            bottomTrigger.isEnabled = false
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(logTag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(logTag, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(logTag, "onRestart")
    }
}
