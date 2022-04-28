package br.edu.ufabc.todostorage.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment(private val field: TextView) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Calendar.getInstance().let {
            DatePickerDialog(
                requireContext(),
                this,
                it.get(Calendar.YEAR),
                it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH)
            )
        }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        field.text = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            .format(GregorianCalendar(year, month, day).time)
    }
}