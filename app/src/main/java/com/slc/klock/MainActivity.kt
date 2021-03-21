package com.slc.klock

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity() {

    private var hour1 = -1
    private var hour2 = -1
    private var min1 = -1
    private var min2 = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivTime1.setOnClickListener { showTimePicker1() }
        ivTime2.setOnClickListener { showTimePicker2() }
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker1(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            hour1 = hour
            min1 = minute
            if (minute == 0)
                tvTime1.text = "$hour:${minute}0h"
            else
                tvTime1.text = "$hour:${minute}h"
            if (hour1 != -1 && hour2 != -1 && min1 != -1 && min2 != -1)
                setOperation()
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker2(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            hour2 = hour
            min2 = minute
            if (minute == 0)
                tvTime2.text = "$hour:${minute}0h"
            else
                tvTime2.text = "$hour:${minute}h"
            if (hour1 != -1 && hour2 != -1 && min1 != -1 && min2 != -1)
                setOperation()
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    private fun setOperation(){
        when {
            hour1 > hour2 -> tvCount.text = calculateDifference(hour1, min1, hour2, min2)
            hour1 < hour2 -> tvCount.text = "- " + calculateDifference(hour2, min2, hour1, min1)
            min1 > min2 -> tvCount.text = calculateDifference(hour1, min1, hour2, min2)
            else -> tvCount.text = "- " + calculateDifference(hour2, min2, hour1, min1)
        }
    }

    private fun calculateDifference(highHour: Int, hightMinute: Int, lowHour: Int, lowMinute: Int): String{
        var hourDiff = -1
        var minDiff = -1

        hourDiff = highHour - lowHour

        if (hightMinute >= lowMinute)
            minDiff = hightMinute - lowMinute
        else {
            minDiff = 60 - lowMinute + hightMinute
            if (hourDiff != 0)
                hourDiff--
        }

        return "${hourDiff}h ${minDiff}min"
    }
}