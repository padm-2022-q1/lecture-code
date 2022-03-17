package br.edu.ufabc.fragmentsdemo

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.demo_list)
        populateList()
    }

    private fun populateList() {
        listView.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.demo_list))
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = when (position) {
                0 -> Intent(this,
                    StaticFragmentActivity::class.java)
                1 -> Intent(this,
                    DynamicFragmentsActivity::class.java)
                2 -> Intent(this,
                    DeviceDetectionActivityDeclarative::class.java)
                else -> Intent(this,
                    DeviceDetectionActivityImperative::class.java)
            }
            intent.also { startActivity(it) }
        }
    }
}
