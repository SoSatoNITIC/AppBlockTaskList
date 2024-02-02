package com.example.appblocktasklist

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.roomdb.locksettingDB.LockSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class SystemLockMenu : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_lockmenu_fragment, container, false)
    }


    var locks: MutableList<LockSetting> = mutableListOf()
    lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel初期化
        sharedViewModel.reset()

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        val listView_lock = view.findViewById<ListView>(R.id.locksettinglist)



        //DB読み込み
        GlobalScope.launch {
            locks = MyApplication.database.lockSettingDao().getAll().toMutableList()

            withContext(Dispatchers.Main) {
                val lockStrings: List<String> = locks.mapNotNull { lock ->
                    val days = lock.dayOfWeek.entries.filter { it.value }.joinToString(", ") { dayOfWeekToJapanese(it.key) }
                    val humanReadableTime = if (lock.usableTime != null) {
                        durationToString(lock.usableTime)
                    } else if (lock.beginTime != null && lock.endTime != null) {
                        "${localTimeToString(lock.beginTime)}～${localTimeToString(lock.endTime)}の間制限"
                    } else {
                        null
                    }

                    val pm: PackageManager = requireContext().packageManager
                    val labelNames = mutableListOf<String>()
                    for (packageName in lock.targetApp) {
                        try {
                            val packageInfo = pm.getPackageInfo(packageName, 0)
                            labelNames.add(pm.getApplicationLabel(packageInfo.applicationInfo).toString())
                        } catch (e: PackageManager.NameNotFoundException) {
                            continue
                        }
                    }

                    val targetApps = labelNames.joinToString(", ") // Join the target apps into a single string
                    if (humanReadableTime != null) {
                        "$humanReadableTime\n 制限する曜日:$days\n 制限アプリ: $targetApps" // Include target apps in the string
                    } else {
                        null
                    }
                }

                adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, lockStrings)
                listView_lock.adapter = adapter
                adapter.notifyDataSetChanged() // Add this line
            }
        }



        //val listView_lock = view.findViewById<ListView>(R.id.locksettinglist)
        listView_lock.setOnItemClickListener { parent, view, position, id ->
            GlobalScope.launch {
                val locks: List<LockSetting> = MyApplication.database.lockSettingDao().getAll()
                val selectedLock = locks[position]
                // Switch to main thread before updating LiveData
                withContext(Dispatchers.Main) {
                    // Use the information from the selected lock to initialize the ViewModel
                    sharedViewModel.setBeginTime(selectedLock.beginTime)
                    sharedViewModel.setEndTime(selectedLock.endTime)
                    sharedViewModel.setUsableTime(selectedLock.usableTime)
                    sharedViewModel.setDayOfWeek(selectedLock.dayOfWeek)
                    sharedViewModel.setTargetApp(selectedLock.targetApp)
                    sharedViewModel.setUnUsableTime(selectedLock.unUsableTime)
                    sharedViewModel.setPreNoticeTiming(selectedLock.preNoticeTiming)
                    sharedViewModel.setActiveDate(selectedLock.activeDate)


                    //DB ID付与
                    sharedViewModel.setLockId(selectedLock.id)



                    // Navigate to the new screen
                    val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToLockSettingDetailsFragment()
                    navController.navigate(action)

                }
            }


        }





        view.findViewById<Button>(R.id.button3).setOnClickListener{
            //ViewModel初期化
            sharedViewModel.reset()

            val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToLockSettingTrigerFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.SystemLockMenuBackButton).setOnClickListener{
            val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToSystemHomeFragment()
            navController.navigate(action)
        }




        // TODOリストビュー更新出来てない
        listView_lock.setOnItemLongClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    val lock = locks[position]
                    locks.removeAt(position)
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch {
                        MyApplication.database.lockSettingDao().delete(lock)
                    }

                    val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToSystemHomeFragment()
                    navController.navigate(action)

                }
                .setNegativeButton("キャンセル", null)
                .setCancelable(true)
            builder.show()
            true
        }



    }

    fun dayOfWeekToJapanese(day: DayOfWeek): String {
        return when (day) {
            DayOfWeek.MONDAY -> "月"
            DayOfWeek.TUESDAY -> "火"
            DayOfWeek.WEDNESDAY -> "水"
            DayOfWeek.THURSDAY -> "木"
            DayOfWeek.FRIDAY -> "金"
            DayOfWeek.SATURDAY -> "土"
            DayOfWeek.SUNDAY -> "日"
        }
    }

    fun durationToString(duration: Duration?): String? {
        if (duration == null) {
            return null
        }

        val hours = duration.seconds / 3600
        val minutes = duration.seconds % 3600 / 60

        return "$hours"+"時間$minutes"+"分経過後にロック"
    }

    fun localTimeToString(localTime: LocalTime?): String? {
        if (localTime == null) {
            return null
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return localTime.format(formatter).replace(":", "時").plus("分")
    }







}