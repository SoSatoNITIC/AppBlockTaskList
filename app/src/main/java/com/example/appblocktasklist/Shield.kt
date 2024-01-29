package com.example.appblocktasklist

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class Shield: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shield)


        val taskkustButton = findViewById<Button>(R.id.tasklist_GO_button)

        taskkustButton.setOnClickListener{
            println("buttonbnm,hnjkm,")
        }
    }
}