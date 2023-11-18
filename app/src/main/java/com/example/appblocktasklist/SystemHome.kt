package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.roomdb.TasksDB.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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


        //ロック設定が押されたら
        view.findViewById<Button>(R.id.button2).setOnClickListener{
            //ファイル名 +  Directionsが自動生成される
            val action = SystemHomeDirections.actionSystemHomeFragmentToSystemLockmenuFragment()
            navController.navigate(action)
        }

        //タスク設定が押されたら
        view.findViewById<Button>(R.id.SystemHomeTaskBottun).setOnClickListener{
            val action = SystemHomeDirections.actionSystemHomeFragmentToSystemTaskmenuFragment()
            navController.navigate(action)
        }
    }
}