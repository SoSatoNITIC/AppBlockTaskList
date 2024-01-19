package com.example.appblocktasklist

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalTime


class LockSettingBetween : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_between_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val timeLabelStart = view.findViewById<TextInputLayout>(R.id.time_label_start)
        val timeViewStart = view.findViewById<TextInputEditText>(R.id.time_start)
        val timePickerActionsStart = view.findViewById<ImageButton>(R.id.time_picker_actions_start)

        val timeLabelEnd = view.findViewById<TextInputLayout>(R.id.time_label_end)
        val timeViewEnd = view.findViewById<TextInputEditText>(R.id.time_end)
        val timePickerActionsEnd = view.findViewById<ImageButton>(R.id.time_picker_actions_end)

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            // val 変数名(好きなの)　= view.findViewById<型名>(R.id.型名の名前ID)　val = view.findViewById<>(R.id.)
            val sunday = view.findViewById<ToggleButton>(R.id.SundayToggleButton).isChecked
            val monday = view.findViewById<ToggleButton>(R.id.MondayToggleButton).isChecked
            val tuesday = view.findViewById<ToggleButton>(R.id.TuesdayToggleButton).isChecked
            val wednesday = view.findViewById<ToggleButton>(R.id.WednesdayToggleButton).isChecked
            val thursday = view.findViewById<ToggleButton>(R.id.ThursdayToggleButton).isChecked
            val friday = view.findViewById<ToggleButton>(R.id.FridayToggleButton).isChecked
            val saturday = view.findViewById<ToggleButton>(R.id.SaturdayToggleButton).isChecked

            val startTime = LocalTime.parse(timeViewStart.text)
            val endTime = LocalTime.parse(timeViewEnd.text)

            val action = LockSettingBetweenDirections.actionLockSettingBetweenFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }



        //開始時刻の時刻設定ダイアログ設定
        timePickerActionsStart.setOnClickListener {
            val currentHour = 12
            val currentMinute = 0
            val is24HourView = true
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    // 時刻設定が完了したときの処理をここに書く
                    timeViewStart.text = Editable.Factory.getInstance().newEditable(String.format("%02d:%02d", hourOfDay, minute))

                },
                currentHour,
                currentMinute,
                is24HourView
            )
            dialog.show()
        }

        //終了時刻の時刻設定ダイアログ設定
        timePickerActionsEnd.setOnClickListener {
            val currentHour = 12
            val currentMinute = 0
            val is24HourView = true
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    // 時刻設定が完了したときの処理をここに書く
                    timeViewEnd.text = Editable.Factory.getInstance().newEditable(String.format("%02d:%02d", hourOfDay, minute))

                },
                currentHour,
                currentMinute,
                is24HourView
            )
            dialog.show()
        }



    }


}