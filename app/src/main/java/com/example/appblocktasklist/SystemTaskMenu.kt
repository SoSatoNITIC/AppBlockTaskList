package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment


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

        view.findViewById<Button>(R.id.SystemTaskMenuAddWantBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingWanFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.systemTaskMenuAddMustBottun).setOnClickListener{
            val action = SystemTaskMenuDirections.actionSystemTaskmenuFragmentToTaskSettingShould()
            navController.navigate(action)
        }
    }
}