package com.example.medicomgmester.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.medicomgmester.MenuActivity
import com.example.medicomgmester.R
import com.example.medicomgmester.model.Appointment
import kotlinx.android.synthetic.main.view_action_bar.*
import java.text.SimpleDateFormat
import java.util.*

class SettingCalendarActivity : AppCompatActivity() {
    companion object {
        const val Key = "KEY_DATA"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_calendar)
        setUI()
        addDateCalendar()
        serEvent()
    }
    private fun setUI(){
        text_bar.text="ตั้งค่า"
    }

    private fun addDateCalendar(){
        val data: Appointment? = intent.getParcelableExtra(Key)
        val startTime: Long
        var endTime: Long
        val startDate = ""+data?.date
        val date: Date = SimpleDateFormat("dd-MM-yyyy").parse(startDate)
        startTime = date.getTime()
        endTime = date.getTime()
        val cal: Calendar = Calendar.getInstance()
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("beginTime", startTime)
        intent.putExtra("allDay", true)
        intent.putExtra("endTime", endTime)
        intent.putExtra("title", data?.name_appointment+"ที่โรงพยาบาลธรรมศาสตร์"+" "+"เวลา: "
                +data?.appointment_time)
        startActivity(intent)
    }

    private fun serEvent() {
        arrow_back.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}