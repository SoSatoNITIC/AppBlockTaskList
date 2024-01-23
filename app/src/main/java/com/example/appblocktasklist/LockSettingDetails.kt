package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment


class LockSettingDetails : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_details_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //NavHostの取得
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //navControllerの取得
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button5).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToSystemLockmenuFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.buttonLockSetting).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToLockSettingTrigerFragment()
            navController.navigate(action)
        }

        view.findViewById<Button>(R.id.buttonApp).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }
        view.findViewById<Button>(R.id.buttonDetail).setOnClickListener{
            val action = LockSettingDetailsDirections.actionLockSettingDetailsFragmentToSystemAdvancedSettingFragment()
            navController.navigate(action)
        }
    }


}