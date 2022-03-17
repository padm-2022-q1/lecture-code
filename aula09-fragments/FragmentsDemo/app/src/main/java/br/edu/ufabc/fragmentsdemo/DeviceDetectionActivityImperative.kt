package br.edu.ufabc.fragmentsdemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DeviceDetectionActivityImperative : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = resources.configuration

        if (config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_XLARGE))
            setContentView(R.layout.activity_device_detection_tablet)
        else
            setContentView(R.layout.activity_device_detection)
    }
}
