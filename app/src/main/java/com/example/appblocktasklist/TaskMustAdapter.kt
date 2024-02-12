package com.example.appblocktasklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.appblocktasklist.roomdb.TasksDB.Task

class TaskMustAdapter (context: Context, taskList: List<Task>):
    ArrayAdapter<Task>(context, R.layout.applistitem2, taskList){
    val taskList = taskList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position]

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.applistitem2, parent, false)

        val textView = view.findViewById<TextView>(R.id.usiwakamaru)
        textView.text = task.taskName
        val textView1 = view.findViewById<TextView>(R.id.minamotonoyositune)

        val deadline = task.deadline
        textView1.text = "期限：$deadline"

        return view
    }
    }

