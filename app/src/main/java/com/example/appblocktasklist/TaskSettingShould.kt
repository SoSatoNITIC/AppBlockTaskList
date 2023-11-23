package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.appblocktasklist.roomdb.TasksDB.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaskSettingShould : Fragment() {
    private val args: TaskSettingWanArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_setting_should, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val titleEditText = view.findViewById<EditText>(R.id.title)
        val titleReasonEditText = view.findViewById<EditText>(R.id.title_reason)
        val reasonOfReasonEditText = view.findViewById<EditText>(R.id.reason_of_reason)
        val memoEditText = view.findViewById<EditText>(R.id.memo)
        val deadlineEditText = view.findViewById<EditText>(R.id.deadline)

        if (args.taskID == -1) {
            view.findViewById<Button>(R.id.button3).setOnClickListener{
                val title = titleEditText.text.toString()
                val titleReason = titleReasonEditText.text.toString()
                val reasonOfReason = reasonOfReasonEditText.text.toString()
                val memo = memoEditText.text.toString()
                val deadline = deadlineEditText.text.toString()

                if (title != ""){
                    GlobalScope.launch {
                        MyApplication.database.tasksDao().insertAll(
                            Task(
                                taskName = title,
                                memo = memo,
                                deadline = deadline,
                                reason = "$titleReason, $reasonOfReason"
                            )
                        )
                    }
                    val action = TaskSettingShouldDirections.actionTaskSettingShouldToSystemHomeFragment()
                    navController.navigate(action)
                }
            }
        } else {
            GlobalScope.launch {
                var task = MyApplication.database.tasksDao().getTask(args.taskID)

                val reasons = task.reason.split(",")

                titleEditText.setText(task.taskName)
                titleReasonEditText.setText(reasons[0])
                reasonOfReasonEditText.setText(reasons[1])
                memoEditText.setText(task.memo)
                deadlineEditText.setText(task.deadline)

                view.findViewById<Button>(R.id.button3).setOnClickListener{
                    val title = titleEditText.text.toString()
                    val titleReason = titleReasonEditText.text.toString()
                    val reasonOfReason = reasonOfReasonEditText.text.toString()
                    val memo = memoEditText.text.toString()
                    val deadline = deadlineEditText.text.toString()

                    if (title != ""){
                        GlobalScope.launch {
                            task.taskName = title
                            task.reason = "${titleReason}, ${reasonOfReason}"
                            task.memo = memo
                            task.deadline = deadline
                            MyApplication.database.tasksDao().update(
                                task
                            )
                        }
                        val action = TaskSettingShouldDirections.actionTaskSettingShouldToSystemHomeFragment()
                        navController.navigate(action)
                    }
                }
            }
        }
    }
}