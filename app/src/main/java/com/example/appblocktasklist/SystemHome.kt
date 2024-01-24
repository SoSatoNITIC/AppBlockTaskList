package com.example.appblocktasklist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.lockProcess.calcRemaining
import com.example.appblocktasklist.lockProcess.setLockWorker
import com.example.appblocktasklist.notify.TimeRemaining
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appblocktasklist.roomdb.TasksDB.Task
import com.example.appblocktasklist.roomdb.rocksettingDB.lockSettingDao
import com.example.appblocktasklist.worker.UsedApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Lock
import java.util.concurrent.TimeUnit

class SystemHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //NavHostの取得
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //navControllerの取得
        val navController = navHostFragment.navController

        GlobalScope.launch {
            val listView = view.findViewById<ListView>(R.id.SystemTaskList)
            val tasks: List<Task> = MyApplication.database.tasksDao().getAll()
            val taskNames: List<String> = tasks.map { it.taskName }

            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, taskNames)
            listView.adapter = adapter
        }

        GlobalScope.launch {
            val listView_lock = view.findViewById<ListView>(R.id.SystemLockList)
            val tasks = MyApplication.database.rocksettingDao().getAll()

            println(tasks)
        }

        if (Settings.canDrawOverlays(requireContext())) {
            println("Work OK1")

            val workRequest = OneTimeWorkRequestBuilder<UsedApp>()
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(requireContext())
                .beginUniqueWork("uniqueWork", ExistingWorkPolicy.KEEP, workRequest)//Workerが複数起動することを防ぐ
                .enqueue()

            println("Work OK2")

        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireContext().packageName)
            )

            requireActivity().startActivity(intent)
            val intent2 = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            requireActivity().startActivity(intent2)
        }


        //ロック設定が押されたら
        view.findViewById<Button>(R.id.button2).setOnClickListener{
//            //ファイル名 +  Directionsが自動生成される
//            val action = SystemHomeDirections.actionSystemHomeFragmentToSystemLockmenuFragment()
//            navController.navigate(action)

            //val remaining = calcRemaining("com.google.android.youtube")
            //if (remaining != null) {
            //    setLockWorker(requireContext(), remaining)
            //}


            //通知送るとき
            //val timeRemaining = TimeRemaining()
            //val remaining = 60
            //timeRemaining.sendRemainingTimeNotification(requireContext(),remaining)
          
          
            println("Work OK1")
            val workRequest = OneTimeWorkRequestBuilder<UsedApp>()
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(requireContext())
                .beginUniqueWork("uniqueWork", ExistingWorkPolicy.KEEP, workRequest)//Workerが複数起動することを防ぐ
                .enqueue()

            println("Work OK2")



            //ファイル名 +  Directionsが自動生成される
            val action = SystemHomeDirections.actionSystemHomeFragmentToSystemLockmenuFragment()
            navController.navigate(action)
        }

        //タスク設定が押されたら
        view.findViewById<Button>(R.id.SystemHomeTaskBottun).setOnClickListener{

            println("Work OK1")
            val workRequest = OneTimeWorkRequestBuilder<UsedApp>()
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(requireContext())
                .beginUniqueWork("uniqueWork", ExistingWorkPolicy.KEEP, workRequest)//Workerが複数起動することを防ぐ
                .enqueue()

            println("Work OK2")



            val action = SystemHomeDirections.actionSystemHomeFragmentToSystemTaskmenuFragment()
            navController.navigate(action)
        }
    }
}