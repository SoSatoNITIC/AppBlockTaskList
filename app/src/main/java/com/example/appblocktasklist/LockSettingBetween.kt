package com.example.appblocktasklist

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.DayOfWeek
import java.time.LocalTime


class LockSettingBetween : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

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

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            val sunday = view.findViewById<Switch>(R.id.Sundayswitch)
            val monday = view.findViewById<Switch>(R.id.Mondayswitch)
            val tuesday = view.findViewById<Switch>(R.id.Tuesdayswitch)
            val wednesday = view.findViewById<Switch>(R.id.Wednesdayswitch)
            val thursday = view.findViewById<Switch>(R.id.Thursdayswitch)
            val friday = view.findViewById<Switch>(R.id.Fridayswitch)
            val saturday = view.findViewById<Switch>(R.id.Saturdayswitch)

            val dayOfWeeks = mapOf<DayOfWeek, Boolean>(
                DayOfWeek.MONDAY to monday.isChecked,
                DayOfWeek.TUESDAY to tuesday.isChecked,
                DayOfWeek.WEDNESDAY to wednesday.isChecked,
                DayOfWeek.THURSDAY to thursday.isChecked,
                DayOfWeek.FRIDAY to friday.isChecked,
                DayOfWeek.SATURDAY to saturday.isChecked,
                DayOfWeek.SUNDAY to sunday.isChecked
            )
            for (i in dayOfWeeks.values) {
                println(i)
            }
            println(!dayOfWeeks.values.any { it })
            // 曜日が1つ以上選択されているかチェック
            if (!dayOfWeeks.values.any { it }) {
                Toast.makeText(requireContext(), "曜日を選択してください", Toast.LENGTH_SHORT).show()

            } else if (timeViewStart.text == null || timeViewStart.text!!.isEmpty()) {
                // timeViewStartのテキストが空でないかチェック

                Toast.makeText(requireContext(), "開始時間を入力してください", Toast.LENGTH_SHORT).show()

            } else if (timeViewEnd.text == null || timeViewEnd.text!!.isEmpty()) {
                // timeViewEndのテキストがnullでないかチェック

                Toast.makeText(requireContext(), "終了時間を入力してください", Toast.LENGTH_SHORT).show()

            } else {
                // 全ての条件が満たされている場合、次のアクションを実行
                sharedViewModel.setDayOfWeek(dayOfWeeks)
                sharedViewModel.setBeginTime(LocalTime.parse(timeViewStart.text))
                sharedViewModel.setBeginTime(LocalTime.parse(timeViewEnd.text))
                val action = LockSettingBetweenDirections.actionLockSettingBetweenFragmentToLockSettingTargetFragment()
                navController.navigate(action)
            }



        }
    }
}