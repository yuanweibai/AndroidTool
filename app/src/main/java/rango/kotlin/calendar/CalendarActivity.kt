package rango.kotlin.calendar

import android.graphics.Color
import android.os.Bundle
import android.os.WorkSource
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calendar_layout.*
import rango.tool.androidtool.R
import rango.tool.common.utils.Worker

class CalendarActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CalendarActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_layout)

        numberPicker.maxValue = 45
        numberPicker.minValue = 0

        spinnerPicker.init(1990, 0, 1) { view, year, monthOfYear, dayOfMonth -> Log.e(TAG, "year = $year, month = $monthOfYear, day = $dayOfMonth") }
//        numberPicker.solidColor = Color.RED

//        DatePickerUtils.resizePikcer(datePicker2)
    }
}