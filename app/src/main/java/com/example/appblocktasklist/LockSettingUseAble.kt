package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.navigation.fragment.NavHostFragment


class LockSettingUseAble : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_lock_setting_useabletime_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            val sunday = view.findViewById<ToggleButton>(R.id.SundayToggleButton)
            val monday = view.findViewById<ToggleButton>(R.id.MondayToggleButton)
            val tuesday = view.findViewById<ToggleButton>(R.id.TuesdayToggleButton)
            val wednesday = view.findViewById<ToggleButton>(R.id.WednesdayToggleButton)
            val thursday = view.findViewById<ToggleButton>(R.id.ThursdayToggleButton)
            val friday = view.findViewById<ToggleButton>(R.id.FridayToggleButton)
            val saturday = view.findViewById<ToggleButton>(R.id.SaturdayToggleButton)
            val action = LockSettingUseAbleDirections.actionLockSettingUseabletimeFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }
    }


}