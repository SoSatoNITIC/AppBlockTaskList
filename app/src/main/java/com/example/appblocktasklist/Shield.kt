package com.example.appblocktasklist

import android.app.Activity
import android.content.Intent
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

        val okbutton = findViewById<Button>(R.id.button2)

        taskkustButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("key", "locked") // ここでロック画面からの遷移
            startActivity(intent)
        }

        okbutton.setOnClickListener{

            val intent2 = Intent(this, MainActivity::class.java)
            intent2.putExtra("key", "locked") // ここでロック画面からの遷移
            startActivity(intent2)


            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


    }
}