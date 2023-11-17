package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.roomdb.TasksDB.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaskSettingWan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_setting_wan_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button7).setOnClickListener{
            val title = view.findViewById<TextInputLayout>(R.id.inputTask1).editText?.text.toString()
            val titleReason = view.findViewById<TextInputLayout>(R.id.inputTask2).editText?.text.toString()
            val reasonOfReason = view.findViewById<TextInputLayout>(R.id.textInputLayout3).editText?.text.toString()
            val memo = view.findViewById<TextInputLayout>(R.id.memoInput).editText?.text.toString()
            GlobalScope.launch {
                MyApplication.database.tasksDao().insertAll(
                    Task(
                        taskName = title,
                        memo = memo,
                        reason = "$titleReason, $reasonOfReason"
                    )
                )
            }
            val action = TaskSettingWanDirections.actionTaskSettingWanFragmentToSystemHomeFragment()
            navController.navigate(action)
        }
    }
}