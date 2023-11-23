package com.example.appblocktasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.appblocktasklist.roomdb.TasksDB.Task

class TaskAdapter(context: Context, taskList: List<Task>):
    ArrayAdapter<Task>(context, android.R.layout.simple_list_item_1, taskList) {
    val taskList = taskList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position]

        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = task.taskName

        return view
    }
}
