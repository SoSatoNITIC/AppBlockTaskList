package com.example.appblocktasklist

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale


class LockSettingDetails : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

    private fun convertEnglishDayToJapanese(day: String): String {
        return when (day.uppercase(Locale.ROOT)) {
            "MONDAY" -> "月"
            "TUESDAY" -> "火"
            "WEDNESDAY" -> "水"
            "THURSDAY" -> "木"
            "FRIDAY" -> "金"
            "SATURDAY" -> "土"
            "SUNDAY" -> "日"
            else -> "不明な曜日"
        }
    }

    private fun convertEnglishDaysToJapanese(days: String): String {
        return days.split(",").map { convertEnglishDayToJapanese(it.trim()) }.joinToString(", ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_details_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationTimings: ArrayList<String> = ArrayList()
        val notificationTimingList: ListView = view.findViewById(R.id.notifyList)

        sharedViewModel.preNoticeTiming.observe(viewLifecycleOwner, Observer { preNoticeTimings ->
            // preNoticeTimingsが更新されたときの処理を書く
            // 例えば、preNoticeTimingsをログに出力する
            println(preNoticeTimings)

            // preNoticeTimingsから時間と分を取得し、指定した形式に変換し、リストに追加
            preNoticeTimings.forEach { duration ->
                val hours = duration.toHoursPart()//バージョン差分で赤線出るかも
                val minutes = duration.toMinutesPart() % 60
                val displayTime = String.format("%d時間%d分前", hours, minutes)
                notificationTimings.add(displayTime)
            }


            val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


            // 画面遷移後にListViewを更新
            coroutineScope.launch {
                delay(100L) // 0.1秒待機
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
                notificationTimingList.adapter = adapter
            }
        })





        //NavHostの取得
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //navControllerの取得
        val navController = navHostFragment.navController

        val appListViewChoose = view.findViewById<ListView>(R.id.choosed_App)

        val packageManager: PackageManager = requireActivity().packageManager

        sharedViewModel.targetApp.observe(viewLifecycleOwner) { apps ->
            // appsは更新された値
            // ここでappsを使ってUIを更新する

            val chosenApps = apps.map { packageManager.getApplicationInfo(it, PackageManager.GET_META_DATA) }

            // ListViewのアダプターを更新
            val adapterChoose = AppListAdapter(requireActivity(), chosenApps)
            appListViewChoose.adapter = adapterChoose

            println(appListViewChoose)
        }

        sharedViewModel.usableTime.observe(viewLifecycleOwner) { usableTime ->
            val textView = view.findViewById<TextView>(R.id.locksettingtexts)
            if(usableTime == null){
                println("Usable null")
                sharedViewModel.beginTime.observe(viewLifecycleOwner) { beginTime ->
                    val begintime = beginTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                    sharedViewModel.endTime.observe(viewLifecycleOwner) { endTime ->
                        val endtime = endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                        textView.append("時間帯型ロック\n開始時間：$begintime\n終了時間：$endtime\n")
                        sharedViewModel.dayOfWeek.observe(viewLifecycleOwner) { dayOfWeek ->
                            val selectedDays = dayOfWeek.entries.filter { it.value }.joinToString(", ") { it.key.name }
                            val japaneseDays = convertEnglishDaysToJapanese(selectedDays)
                            textView.append("制限する曜日：$japaneseDays\n")
                        }
                    }
                }
            } else {
                println("Begin and End Time null")
                sharedViewModel.usableTime.observe(viewLifecycleOwner) { usableTime ->
                    val hours = usableTime?.toHours() ?: 0
                    val minutes = usableTime?.toMinutesPart() ?: 0
                    val usableTimes = String.format("%02d時間%02d分", hours, minutes)
                    textView.append("使用時間型ロック\n使用時間：$usableTimes　経過したら制限\n")
                    sharedViewModel.dayOfWeek.observe(viewLifecycleOwner) { dayOfWeek ->
                        val selectedDays = dayOfWeek.entries.filter { it.value }.joinToString(", ") { it.key.name }
                        val japaneseDays = convertEnglishDaysToJapanese(selectedDays)
                        textView.append("選択された曜日：$japaneseDays\n")
                    }
                }
            }
        }





        sharedViewModel.lockid.observe(viewLifecycleOwner, Observer<Int?> { lockid ->
            println(lockid)
        })




        view.findViewById<Button>(R.id.button5).setOnClickListener{


            //sharedViewModel.saveSettings()
            CoroutineScope(Dispatchers.IO).launch {
                sharedViewModel.saveSettings()
            }
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToSystemLockmenuFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.buttonLockSetting).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToLockSettingTrigerFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.buttonApp).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }
        view.findViewById<Button>(R.id.buttonDetail).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToSystemAdvancedSettingFragment()
            navController.navigate(action)
        }
    }


}