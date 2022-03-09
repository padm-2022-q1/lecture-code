package br.edu.ufabc.imccalculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

/**
 * TODO: efetuar a validação dos dados
 * Ref: https://material.io/components/text-fields/android#using-text-fields
 */
class MainActivity : AppCompatActivity() {
    private lateinit var edittextHeight: TextInputEditText
    private lateinit var edittextWeight: TextInputEditText
    private lateinit var buttonCalculate: Button
    private lateinit var textViewIMCValue: TextView
    private lateinit var textViewIMCStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initComponents()
        bindEvents()
    }

    private fun initComponents() {
        edittextHeight = findViewById(R.id.edittext_height_m)
        edittextWeight = findViewById(R.id.edittext_weight_kg)
        buttonCalculate = findViewById(R.id.button_calculate)
        textViewIMCValue = findViewById(R.id.text_imc_value)
        textViewIMCStatus = findViewById(R.id.text_imc_status)
    }

    private fun bindEvents() {
        buttonCalculate.setOnClickListener {
            val height = edittextHeight.text.toString().toFloat()
            val weight = edittextWeight.text.toString().toFloat()
            val imc = weight / (height * height)
            val imcStatus = when {
                imc >= 0 && imc < 17 -> getString(R.string.imc_status_muito_abaixo_peso)
                imc >= 17 && imc < 18.5 -> getString(R.string.imc_status_abaixo_peso)
                imc >= 18.5 && imc < 25 -> getString(R.string.imc_status_peso_normal)
                imc >= 25 && imc < 30 -> getString(R.string.imc_status_acima_peso)
                imc >= 30 && imc < 35 -> getString(R.string.imc_status_obesidade_i)
                imc >= 35 && imc < 40 -> getString(R.string.imc_status_obesidade_ii)
                else -> getString(R.string.imc_status_obesidade_iii)
            }

            textViewIMCValue.text = getString(R.string.imc_value_formatted, imc)
            textViewIMCStatus.text = imcStatus
        }
    }
}