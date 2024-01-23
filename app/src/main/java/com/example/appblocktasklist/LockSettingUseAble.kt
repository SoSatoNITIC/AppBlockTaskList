package com.example.appblocktasklist

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import java.time.DayOfWeek
import java.time.Duration
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

        //ViewModel更新
        sharedViewModel.usableTime.observe(viewLifecycleOwner, { usableTime ->
            val hours = usableTime?.toHours() ?: 0
            val minutes = usableTime?.toMinutesPart() ?: 0
            view.findViewById<EditText>(R.id.time_timer).setText(String.format("%02d時間%02d分", hours, minutes))
        })

        sharedViewModel.dayOfWeek.observe(viewLifecycleOwner, { states ->
            states?.forEach { (dayOfWeek, isChecked) ->
                when (dayOfWeek) {
                    // Switchの状態を更新
                    DayOfWeek.SUNDAY -> view.findViewById<Switch>(R.id.Sundayswitch).isChecked = isChecked
                    DayOfWeek.MONDAY -> view.findViewById<Switch>(R.id.Mondayswitch).isChecked = isChecked
                    DayOfWeek.TUESDAY -> view.findViewById<Switch>(R.id.Tuesdayswitch).isChecked = isChecked
                    DayOfWeek.WEDNESDAY -> view.findViewById<Switch>(R.id.Wednesdayswitch).isChecked = isChecked
                    DayOfWeek.THURSDAY -> view.findViewById<Switch>(R.id.Thursdayswitch).isChecked = isChecked
                    DayOfWeek.FRIDAY -> view.findViewById<Switch>(R.id.Fridayswitch).isChecked = isChecked
                    DayOfWeek.SATURDAY -> view.findViewById<Switch>(R.id.Saturdayswitch).isChecked = isChecked

                }
            }
        })


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
            val timeInput = view.findViewById<EditText>(R.id.time_timer)

            //曜日が一つ以上選択されているかチェック
            if (!dayOfWeeks.values.any{ it }){
                Toast.makeText(requireContext(), "曜日を選択してください", Toast.LENGTH_SHORT).show()
            }else if (timeInput.text == null || timeInput.text!!.isEmpty() || timeInput.text.toString() == "00時間00分") {
                Toast.makeText(requireContext(), "時間を設定してください", Toast.LENGTH_SHORT).show()
            }  else {
                sharedViewModel.setDayOfWeek(dayOfWeeks)

                //Duration型に合うように変換
                val timeStr = timeInput.text.toString()
                val parts = timeStr.split("時間")
                val partsMinuts = parts.last().split("分")
                println(parts)
                //val hours = parts.first().trim().toInt()
                //val minutes = partsMinuts.first().trim().toInt()
                val hours = parts.first().trim().toLong()
                val minutes = partsMinuts.first().trim().toLong()
                //val durationStr = "PT${hours}H${minutes}M"
                //val duration = Duration.parse(durationStr)
                val duration = Duration.ofHours(hours).plusMinutes(minutes)//Duration微修正
                sharedViewModel.setUsableTime(duration)

                println(duration)

                val action = LockSettingUseAbleDirections.actionLockSettingUseabletimeFragmentToLockSettingTargetFragment()
                navController.navigate(action)


            }


        }



        val timePickerButton = view.findViewById<ImageButton>(R.id.time_picker_actions_timer)
        timePickerButton.setOnClickListener {
            val timeInput = view.findViewById<EditText>(R.id.time_timer)
            showTimePicker(timeInput)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showTimePicker(timeInput: EditText) {
        // ViewModelから時間を取得します。
        var hourOfDay = 0
        var minutes = 0
        val usableTime = sharedViewModel.usableTime.value
        if (usableTime != null) {
            //赤線出るかもだけどバージョン差分で出てる
            hourOfDay = usableTime.toHours().toInt()
            minutes = usableTime.toMinutesPart().toInt()
        }

        // TimePickerDialogを作成します。初期値は上記で取得した時間に設定します。
        val picker = TimePickerDialog(
            timeInput.rootView.context,
            AlertDialog.THEME_HOLO_LIGHT,
            { _, getHour, getMinutes ->
                // 時刻設定が完了したときの処理をここに書く
                if(getHour!=0 || getMinutes!=0){
                    // 選択された時間をEditTextに設定します。
                    timeInput.setText(String.format("%02d時間%02d分", getHour, getMinutes))
                }else{
                    // 時間が0分未満の場合はエラーメッセージを表示します。
                    Toast.makeText(requireContext(), "0分以上の時間を指定してください", Toast.LENGTH_SHORT).show()
                }
            },
            hourOfDay, // 初期値として時間を設定します。
            minutes,    // 初期値として分を設定します。
            true        // 24時間形式を使用するかどうかを指定します。
        )

        // TimePickerDialogを表示します。
        picker.show()
    }


    private val TIME_FORMAT = "yyyy/MM/dd HH:mm"

    fun toLocaleTime(stringTime: String): LocalDateTime? {
        val df = DateTimeFormatter.ofPattern(TIME_FORMAT)
        return try { LocalDateTime.parse("2001/01/01 " + stringTime, df) } catch (t: Throwable) { null }
    }


}