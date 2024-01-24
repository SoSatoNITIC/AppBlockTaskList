package com.example.appblocktasklist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.util.Locale


class LockSettingDetails : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

    // chosenAppIconsとchosenAppNamesをフラグメントのプロパティとして定義
    private val chosenAppIcons = ArrayList<Drawable>()
    private val chosenAppNames = ArrayList<String>()




    fun convertEnglishDayToJapanese(day: String): String {
        return when (day.toUpperCase()) {
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

    fun convertEnglishDaysToJapanese(days: String): String {
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

        val installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // システムアプリを除外する Youtube関係は保持
        val userApps = installedApplications.filter { appInfo ->
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    appInfo.packageName == "com.google.android.youtube" ||
                    appInfo.packageName == "com.google.android.apps.youtube.music" ||
                    appInfo.packageName == "com.android.chrome"
        }
        //AppNameとIcon表示
        val appNames = userApps.map { it.loadLabel(packageManager).toString() }

        val appIcons = userApps.map { it.loadIcon(packageManager) }

        val appKey = appNames.zip(appIcons).toMap()

        sharedViewModel.targetApp.observe(viewLifecycleOwner, Observer { apps ->
            // appsは更新された値
            // ここでappsを使ってUIを更新する

            // 選択されたアプリの名前とアイコンを取得
            val appNames = apps.map { appName -> appName }
            val appIcons = apps.map { appName -> appKey[appName] ?: ColorDrawable() }


            // chosenAppNamesとchosenAppIconsを更新
            chosenAppNames.clear()
            chosenAppNames.addAll(appNames)
            chosenAppIcons.clear()
            chosenAppIcons.addAll(appIcons)

            // ListViewのアダプターを更新
            val adapterChoose = AppListAdapter(requireActivity(), chosenAppIcons, chosenAppNames)
            appListViewChoose.adapter = adapterChoose


            println(appListViewChoose)
        })





        sharedViewModel.usableTime.observe(viewLifecycleOwner, { usableTime ->
            if(usableTime == null){
                println("Usable null")
                val lockset = "時間帯型ロック"
                var timeString = " "
                sharedViewModel.beginTime.observe(viewLifecycleOwner, { beginTime ->
                    val begintime = beginTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                    sharedViewModel.endTime.observe(viewLifecycleOwner, { endTime ->
                        val endtime = endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                        timeString += "時間帯型ロック\n開始時間：$begintime\n終了時間：$endtime\n"

                        sharedViewModel.dayOfWeek.observe(viewLifecycleOwner, { dayOfWeek ->
                            val selectedDays = dayOfWeek.entries.filter { it.value }.joinToString(", ") { it.key.name }
                            println(selectedDays)
                            val japaneseDays = convertEnglishDaysToJapanese(selectedDays)
                            timeString += "制限する曜日：$japaneseDays\n"



                            val textView = view.findViewById<TextView>(R.id.locksettingtexts)
                            textView.text = timeString

                        })

                        println(timeString)
                        //val textView = view.findViewById<TextView>(R.id.lockSetting)
                        //textView.text = timeString

                    })
                })

                
            }else{
                println("Begin and End Time null")
                var timeString = " "
                sharedViewModel.usableTime.observe(viewLifecycleOwner, { usableTime ->
                    val hours = usableTime?.toHours() ?: 0
                    val minutes = usableTime?.toMinutesPart() ?: 0
                    val usableTimes = String.format("%02d時間%02d分", hours, minutes)
                    timeString += "使用時間型ロック\n使用時間：$usableTimes　経過したら制限\n"
                    sharedViewModel.dayOfWeek.observe(viewLifecycleOwner, { dayOfWeek ->
                        val selectedDays = dayOfWeek.entries.filter { it.value }.joinToString(", ") { it.key.name }
                        println(selectedDays)
                        val japaneseDays = convertEnglishDaysToJapanese(selectedDays)
                        timeString += "選択された曜日：$japaneseDays\n"


                        val textView = view.findViewById<TextView>(R.id.locksettingtexts)
                        textView.text = timeString

                    })
                })
            }
        })







        view.findViewById<Button>(R.id.button5).setOnClickListener{
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