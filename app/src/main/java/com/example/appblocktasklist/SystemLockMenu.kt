package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment


class SystemLockMenu : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_lockmenu_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            //ViewModel初期化
            sharedViewModel.reset()

            val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToLockSettingTrigerFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.SystemLockMenuBackButton).setOnClickListener{
            val action = SystemLockMenuDirections.actionSystemLockmenuFragmentToSystemHomeFragment()
            navController.navigate(action)
        }
    }



}