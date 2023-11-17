package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputEditText


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
            val wanTaskTitle = view.findViewById<TextInputEditText>(R.id.inputTask1)
            val wanTaskReason = view.findViewById<TextInputEditText>(R.id.inputTask2)
            val action = TaskSettingWanDirections.actionTaskSettingWanFragmentToSystemHomeFragment()

            navController.navigate(action)
        }
    }
}