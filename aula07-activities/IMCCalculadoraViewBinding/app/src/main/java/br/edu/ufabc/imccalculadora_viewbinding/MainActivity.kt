package br.edu.ufabc.imccalculadora_viewbinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ufabc.imccalculadora_viewbinding.databinding.ActivityMainBinding

/**
 * TODO: efetuar a validação dos dados
 * Ref: https://material.io/components/text-fields/android#using-text-fields
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        bindEvents()
    }

    private fun bindEvents() {
        binding.buttonCalculate.setOnClickListener {
            val height = binding.edittextHeightM.text.toString().toFloat()
            val weight = binding.edittextWeightKg.text.toString().toFloat()
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
            binding.textImcValue.text = getString(R.string.imc_value_formatted, imc)
            binding.textImcStatus.text = imcStatus
        }
    }
}