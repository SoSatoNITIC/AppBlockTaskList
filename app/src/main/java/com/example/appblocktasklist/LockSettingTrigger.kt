package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment


class LockSettingTrigger : Fragment() {


    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_triger_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        //時間帯型押されたら
        view.findViewById<Button>(R.id.LockSettingBetweenButton).setOnClickListener{
            val action = LockSettingTriggerDirections.actionLockSettingTrigerFragmentToLockSettingBetweenFragment()
            navController.navigate(action)
        }

        //使用時間型押されたら
        view.findViewById<Button>(R.id.button4).setOnClickListener{
            val action = LockSettingTriggerDirections.actionLockSettingTrigerFragmentToLockSettingUseabletimeFragment()
            navController.navigate(action)
        }
    }
}