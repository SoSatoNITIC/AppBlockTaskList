package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.NavHostFragment


class TaskSettingShould : Fragment() {


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

        view.findViewById<Button>(R.id.button3).setOnClickListener{

            val title = view.findViewById<EditText>(R.id.title).text.toString()
            val titleReason = view.findViewById<EditText>(R.id.title_reason).text.toString()
            val reasonOfReason = view.findViewById<EditText>(R.id.reason_of_reason).text.toString()
            val memo = view.findViewById<EditText>(R.id.memo).text.toString()
            val kigen = view.findViewById<EditText>(R.id.kigen).text.toString()
            val action = TaskSettingShouldDirections.actionTaskSettingShouldToSystemHomeFragment()
            navController.navigate(action)
        }
    }
}