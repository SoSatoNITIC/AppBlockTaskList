package com.example.appblocktasklist

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val addWantButton = view.findViewById<Button>(R.id.SystemTaskMenuAddWantBottun)
        val addMustBottun = view.findViewById<Button>(R.id.systemTaskMenuAddMustBottun)

        addWantButton.setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingWanFragment(-1)
            navController.navigate(action)
        }

        addMustBottun.setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingShould(-1)
            navController.navigate(action)
        }

        val listViewWant = view.findViewById<ListView>(R.id.SystemTaskWantList)
        val listViewMust = view.findViewById<ListView>(R.id.SystemTaskMustList)

        val tasksWant: ArrayList<Task> = arrayListOf()
        val tasksMust: ArrayList<Task> = arrayListOf()

        val adapterWant = TaskAdapter(requireContext(), tasksWant)
        val adapterMust = TaskAdapter(requireContext(), tasksMust)

        listViewWant.adapter = adapterWant
        listViewMust.adapter = adapterMust

        GlobalScope.launch {
            val tasks: List<Task> = MyApplication.database.tasksDao().getAll()
            tasksWant.clear()
            tasksWant.addAll(tasks.filter { it.deadline == null })
            tasksMust.clear()
            tasksMust.addAll(tasks.filter { it.deadline != null })

            adapterWant.notifyDataSetChanged()
            adapterMust.notifyDataSetChanged()
        }

        setupLongClickListenerOnListView(listViewMust, tasksMust, adapterMust)
        setupLongClickListenerOnListView(listViewWant, tasksWant, adapterWant)

        listViewWant.setOnItemClickListener { parent, view, position, id ->
            val task = tasksWant[position]
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingWanFragment(task.id)
            navController.navigate(action)
        }

        listViewMust.setOnItemClickListener { parent, view, position, id ->
            val task = tasksMust[position]
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingShould(task.id)
            navController.navigate(action)
        }

    }

    private fun setupLongClickListenerOnListView(listView: ListView, tasks: ArrayList<Task>, adapter: TaskAdapter) {
        listView.setOnItemLongClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    val task = tasks[position]
                    tasks.removeAt(position)
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch {
                        MyApplication.database.tasksDao().delete(task)
                    }
                }
                .setNegativeButton("キャンセル", null)
                .setCancelable(true)
            builder.show()
            true
        }
    }
}