package com.example.appblocktasklist

import android.app.AlertDialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        val listViewWant = view.findViewById<ListView>(R.id.SystemTaskWantList)
        val listViewMust = view.findViewById<ListView>(R.id.SystemTaskMustList)

        val taskNamesWant: ArrayList<Task> = arrayListOf()
        val taskNamesMust: ArrayList<Task> = arrayListOf()

        val adapterWant = TaskAdapter(requireContext(), taskNamesWant)
        val adapterMust = TaskAdapter(requireContext(), taskNamesMust)

        listViewWant.adapter = adapterWant
        listViewMust.adapter = adapterMust

        GlobalScope.launch {
            val tasks: List<Task> = MyApplication.database.tasksDao().getAll()
            taskNamesWant.clear()
            taskNamesWant.addAll(tasks.filter { it.deadline == null })
            taskNamesMust.clear()
            taskNamesMust.addAll(tasks.filter { it.deadline != null })

            adapterWant.notifyDataSetChanged()
            adapterMust.notifyDataSetChanged()
        }


        view.findViewById<Button>(R.id.SystemTaskMenuAddWantBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingWanFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.systemTaskMenuAddMustBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingShould()
            navController.navigate(action)
        }
        listViewMust.setOnItemLongClickListener() { parent, view, position, id ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    val task = taskNamesMust[position]
                    taskNamesMust.removeAt(position)
                    adapterMust.notifyDataSetChanged()

                    GlobalScope.launch {
                        MyApplication.database.tasksDao().delete(task)
                    }
                }
                .setNegativeButton("キャンセル", null)
                .setCancelable(true)
            // show dialog
            builder.show()
            true
        }

        listViewWant.setOnItemLongClickListener() { parent, view, position, id ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    val task = taskNamesWant[position]
                    taskNamesWant.removeAt(position)
                    adapterWant.notifyDataSetChanged()

                    GlobalScope.launch {
                        MyApplication.database.tasksDao().delete(task)
                    }
                }
                .setNegativeButton("キャンセル", null)
                .setCancelable(true)
            // show dialog
            builder.show()
            true
        }
    }
}