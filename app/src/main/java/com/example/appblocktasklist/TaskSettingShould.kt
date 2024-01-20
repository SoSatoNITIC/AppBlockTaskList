package com.example.appblocktasklist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.appblocktasklist.roomdb.TasksDB.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
        var selectedDateTime: String = ""
        val deadlineTextView: TextView = view.findViewById(R.id.textViewDeadline)





        view.findViewById<Button>(R.id.dateButton).setOnClickListener{

            deadlineTextView.text = ""

            //現在の日付と時間を取得
            val now = Calendar.getInstance()

            //日付設定
            val yesterdayCalendar = Calendar.getInstance().apply { add(Calendar.DATE, -1) }//昨日の日付を取得
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(y, m, d)

                    if (!selectedDate.before(yesterdayCalendar)) {
                        // 選択された日付が現在の日付以降である場合の処理
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val dateStr = simpleDateFormat.format(selectedDate.time)

                        val today = SimpleDateFormat("yyyy-MM-dd").format(now.time)

                        if (dateStr == today){//今日なら、過去の時間選べないようにする
                            //println("today")

                            //時刻設定
                            val calendartime = Calendar.getInstance()
                            val hour = calendartime.get(Calendar.HOUR_OF_DAY)
                            val minute = calendartime.get(Calendar.MINUTE)

                            val now = Calendar.getInstance()

                            val timePickerDialog = TimePickerDialog(requireContext(),
                                TimePickerDialog.OnTimeSetListener { _, h, m ->
                                    // ユーザーが選択した時間をここで処理
                                    val selectedTime = Calendar.getInstance()
                                    selectedTime.set(Calendar.HOUR_OF_DAY, h)
                                    selectedTime.set(Calendar.MINUTE, m)

                                    if (!selectedTime.before(now)) {
                                        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val timeStr = simpleDateFormat.format(selectedTime.time)

                                        deadlineTextView.append("$dateStr $timeStr")
                                    }
                                }, hour, minute, true)
                            timePickerDialog.show()


                        }else{
                            //時刻設定
                            val calendartime = Calendar.getInstance()
                            val hour = calendartime.get(Calendar.HOUR_OF_DAY)
                            val minute = calendartime.get(Calendar.MINUTE)

                            val timePickerDialog = TimePickerDialog(requireContext(),
                                TimePickerDialog.OnTimeSetListener { _, h, m ->
                                    // ユーザーが選択した時間をここで処理
                                    val selectedTime = Calendar.getInstance()
                                    selectedTime.set(Calendar.HOUR_OF_DAY, h)
                                    selectedTime.set(Calendar.MINUTE, m)

                                    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    val timeStr = simpleDateFormat.format(selectedTime.time)

                                    deadlineTextView.append("$dateStr $timeStr")
                                }, hour, minute, true)
                            timePickerDialog.show()
                        }



                    }
                }, year, month, day)


            // 現在の日付をDatePickerDialogの最小日付として設定
            datePickerDialog.datePicker.minDate = now.timeInMillis
            datePickerDialog.show()
        }









        val deadlineEditText = view.findViewById<TextView>(R.id.textViewDeadline)
        if (args.taskID == -1) {
            view.findViewById<Button>(R.id.button3).setOnClickListener{
                val title = titleEditText.text.toString()
                val titleReason = titleReasonEditText.text.toString()
                val reasonOfReason = reasonOfReasonEditText.text.toString()
                val memo = memoEditText.text.toString()
                val deadline = deadlineEditText.text.toString()

                if (title != "" && deadline != ""){
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

                    if (title != "" && deadline != ""){
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