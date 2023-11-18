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


class SystemTaskMenu : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_taskmenu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        GlobalScope.launch {
            val listViewWant = view.findViewById<ListView>(R.id.SystemTaskWantList)
            val listViewMust = view.findViewById<ListView>(R.id.SystemTaskMustList)

            val tasks: List<Task> = MyApplication.database.tasksDao().getAll()
            val taskNamesWant: List<String> = tasks.filter { it.deadline == null }.map { it.taskName }
            val taskNamesMust: List<String> = tasks.filter { it.deadline != null }.map { it.taskName }

            val adapterWant = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, taskNamesWant)
            val adapterMust = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, taskNamesMust)
            listViewWant.adapter = adapterWant
            listViewMust.adapter = adapterMust
        }


        view.findViewById<Button>(R.id.SystemTaskMenuAddWantBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingWanFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.systemTaskMenuAddMustBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingShould()
            navController.navigate(action)
        }
    }
}