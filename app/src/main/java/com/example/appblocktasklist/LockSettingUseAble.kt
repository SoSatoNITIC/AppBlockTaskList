package com.example.appblocktasklist

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LockSettingUseAble : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_lock_setting_useabletime_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            val sunday = view.findViewById<ToggleButton>(R.id.SundayToggleButton).isChecked
            val monday = view.findViewById<ToggleButton>(R.id.MondayToggleButton).isChecked
            val tuesday = view.findViewById<ToggleButton>(R.id.TuesdayToggleButton).isChecked
            val wednesday = view.findViewById<ToggleButton>(R.id.WednesdayToggleButton).isChecked
            val thursday = view.findViewById<ToggleButton>(R.id.ThursdayToggleButton).isChecked
            val friday = view.findViewById<ToggleButton>(R.id.FridayToggleButton).isChecked
            val saturday = view.findViewById<ToggleButton>(R.id.SaturdayToggleButton).isChecked
            val action = LockSettingUseAbleDirections.actionLockSettingUseabletimeFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }



        val timePickerButton = view.findViewById<ImageButton>(R.id.time_picker_actions_timer)
        timePickerButton.setOnClickListener {
            val timeInput = view.findViewById<EditText>(R.id.time_timer)
            showTimePicker(timeInput)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showTimePicker(timeInput: EditText) {
        var hourOfDay = 0
        var minutes = 0
        val ldt = toLocaleTime(timeInput.text.toString())
        if (ldt != null) {
            hourOfDay = ldt.hour
            minutes = ldt.minute
        }
        val picker = TimePickerDialog(
            timeInput.rootView.context,
            AlertDialog.THEME_HOLO_LIGHT,
            { _, getHour, getMinutes ->
                timeInput.setText(String.format("%02d時間　%02d分", getHour, getMinutes))
            },
            hourOfDay,
            minutes,
            true
        )
        picker.show()
    }

    private val TIME_FORMAT = "yyyy/MM/dd HH:mm"

    fun toLocaleTime(stringTime: String): LocalDateTime? {
        val df = DateTimeFormatter.ofPattern(TIME_FORMAT)
        return try { LocalDateTime.parse("2001/01/01 " + stringTime, df) } catch (t: Throwable) { null }
    }


}