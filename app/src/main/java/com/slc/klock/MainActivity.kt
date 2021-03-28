package com.slc.klock

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Window
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var hour1 = -1
    private var hour2 = -1
    private var min1 = -1
    private var min2 = -1
    private var hourCount = 0
    private var minCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivTime1.setOnClickListener { showTimePicker1() }
        ivTime2.setOnClickListener { showTimePicker2() }
        tvCount.setOnClickListener { showTimeIncrement() }

        ivTime1.setOnLongClickListener {
            resetTime1()
            resetCount()
            true
        }
        ivTime2.setOnLongClickListener {
            resetTime2()
            resetCount()
            true
        }
        tvCount.setOnLongClickListener {
            resetCount()
            resetTime1()
            resetTime2()
            true
        }
    }

    private fun resetTime1(){
        tvTime1.text = resources.getString(R.string.select_time)
        hour1 = -1
        min1 = -1
    }

    private fun resetTime2(){
        tvTime2.text = resources.getString(R.string.select_time)
        hour2 = -1
        min2 = -1
    }

    private fun resetCount(){
        tvCount.text = resources.getString(R.string.default_time)
        hourCount = 0
        minCount = 0
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker1(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            hour1 = hour
            min1 = minute
            when {
                minute == 0 -> tvTime1.text = "$hour:${minute}0"
                minute < 10 -> tvTime1.text = "$hour:0${minute}"
                else -> tvTime1.text = "$hour:${minute}"
            }
            if (hour1 != -1 && hour2 != -1 && min1 != -1 && min2 != -1)
                setOperation()
            else if (hourCount != 0 || minCount != 0)
                incrementTime()
        }
        TimePickerDialog(this, R.style.PinkDialog, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker2(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            hour2 = hour
            min2 = minute
            when {
                minute == 0 -> tvTime2.text = "$hour:${minute}0"
                minute < 10 -> tvTime2.text = "$hour:0${minute}"
                else -> tvTime2.text = "$hour:${minute}"
            }
            if (hour1 != -1 && hour2 != -1 && min1 != -1 && min2 != -1)
                setOperation()
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showTimeIncrement(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_time_increment)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val npHour = dialog.findViewById(R.id.npHour) as NumberPicker
        val npMinutes = dialog.findViewById(R.id.npMinutes) as NumberPicker
        val btnAccept = dialog.findViewById(R.id.tvAccept) as TextView

        npHour.minValue = 0
        npHour.maxValue = 23
        npMinutes.minValue = 0
        npMinutes.maxValue = 59
        hourCount = 0
        minCount = 0

        npHour.setOnValueChangedListener { _, _, newVal ->
            hourCount = newVal
        }

        npMinutes.setOnValueChangedListener { _, _, newVal ->
            minCount = newVal
        }

        btnAccept.setOnClickListener {
            tvCount.text = "${hourCount}h ${minCount}min"
            if (hourCount == 0 && minCount == 0)
                tvTime2.text = resources.getString(R.string.select_time)
            else
                incrementTime()
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun incrementTime(){
        if (hour1 != -1 && min1 != -1){
            hour2 = hour1 + hourCount
            min2 = min1 + minCount
            if (min2 > 59){
                min2 -= 60
                hour2++
            }
            if (hour2 > 23)
                hour2 -= 24
            if (min2 < 10)
                tvTime2.text = "$hour2:0${min2}"
            else
                tvTime2.text = "$hour2:${min2}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOperation(){
        when {
            hour1 == hour2 && min1 == min2 -> tvCount.text = resources.getString(R.string.default_time)
            hour1 > hour2 -> tvCount.text = calculateDifference(hour1, min1, hour2, min2)
            hour1 < hour2 -> tvCount.text = /*"- " +*/ calculateDifference(hour2, min2, hour1, min1)
            min1 > min2 -> tvCount.text = calculateDifference(hour1, min1, hour2, min2)
            else -> tvCount.text = /*"- " +*/ calculateDifference(hour2, min2, hour1, min1)
        }
    }

    private fun calculateDifference(highHour: Int, hightMinute: Int, lowHour: Int, lowMinute: Int): String{
        var hourDiff: Int = highHour - lowHour
        val minDiff: Int

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