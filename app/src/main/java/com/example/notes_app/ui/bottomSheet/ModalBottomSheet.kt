package com.example.notes_app.ui.bottomSheet

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.notes_app.databinding.BottomSheetBinding
import com.example.notes_app.receivers.AlarmReceiver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class ModalBottomSheet : BottomSheetDialogFragment() , TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {

    private lateinit var m_binding  : BottomSheetBinding
    private
    var m_calendar : Calendar = Calendar.getInstance()
    private var isDateSet = false
    private var isTimeSet = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set up the binding
        m_binding = BottomSheetBinding.inflate(layoutInflater)

        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setAllOnclick()


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)

        return dialog
    }



    fun setAllOnclick(){
        m_binding.bottomSheetAddAlarmCancelTv.setOnClickListener {
            this.dialog?.dismiss()
        }
        m_binding.bottomSheetAddAlarmDoneTv.setOnClickListener {

            makeAlarm()

            this.dialog?.dismiss()
        }
        m_binding.bottomSheetAddAlarmTimeTiet.setOnClickListener {
            TimePickerDialog(requireContext() , this , m_calendar[Calendar.HOUR] , m_calendar[Calendar.MINUTE] ,  true).show()
        }
        m_binding.bottomSheetAddAlarmDateTiet.setOnClickListener {
            DatePickerDialog(requireContext() , this , m_calendar[Calendar.YEAR] , m_calendar[Calendar.MONDAY] , m_calendar[Calendar.DAY_OF_MONTH]).show()
        }
    }



    private fun makeAlarm(){
        if(!isTimeSet || !isDateSet){
            Toast.makeText(requireContext() , "set time and date " , Toast.LENGTH_LONG).show()
            return
        }
        var alarmManager = requireActivity().getSystemService(AlarmManager::class.java)


        Toast.makeText(requireContext() , "year : ${m_calendar[Calendar.YEAR]}\nmonth : ${m_calendar[Calendar.MONTH]}\nDAY : ${m_calendar[Calendar.DAY_OF_MONTH]}\nhour : ${m_calendar[Calendar.HOUR_OF_DAY]}\nminute : ${m_calendar[Calendar.MINUTE]}\n" , Toast.LENGTH_LONG).show()

        var intent = Intent(requireActivity() , AlarmReceiver::class.java)
        var pending = PendingIntent.getBroadcast(requireActivity() , 0 , intent, 0)

        m_calendar[Calendar.SECOND] = 0

        val alarmType: Int = AlarmManager.RTC_WAKEUP
        alarmManager.setExactAndAllowWhileIdle(alarmType, m_calendar.timeInMillis, pending)

        isTimeSet=false
        isDateSet=false
    }


    companion object {
        fun newInstance():ModalBottomSheet= ModalBottomSheet()
    }



    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        isTimeSet = true
        m_calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        m_calendar[Calendar.MINUTE] = minute

        var time= String.format("%02d : %02d",hourOfDay , minute)
        m_binding.bottomSheetAddAlarmTimeTiet.setText(time)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        isDateSet = true
        m_calendar[Calendar.MONTH] = month
        m_calendar[Calendar.YEAR] = year
        m_calendar[Calendar.DAY_OF_MONTH] = dayOfMonth

        val date = String.format("%02d / %02d / %02d", dayOfMonth, month + 1,year % 100)
        m_binding.bottomSheetAddAlarmDateTiet.setText(date)
    }



}