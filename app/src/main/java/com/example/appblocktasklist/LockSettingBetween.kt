package com.example.appblocktasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.navigation.fragment.NavHostFragment


class LockSettingBetween : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_between_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            // val 変数名(好きなの)　= view.findViewById<型名>(R.id.型名の名前ID)　val = view.findViewById<>(R.id.)
            val sunday = view.findViewById<ToggleButton>(R.id.SundayToggleButton).isChecked
            val monday = view.findViewById<ToggleButton>(R.id.MondayToggleButton).isChecked
            val tuesday = view.findViewById<ToggleButton>(R.id.TuesdayToggleButton).isChecked
            val wednesday = view.findViewById<ToggleButton>(R.id.WednesdayToggleButton).isChecked
            val thursday = view.findViewById<ToggleButton>(R.id.ThursdayToggleButton).isChecked
            val friday = view.findViewById<ToggleButton>(R.id.FridayToggleButton).isChecked
            val saturday = view.findViewById<ToggleButton>(R.id.SaturdayToggleButton).isChecked
            val action = LockSettingBetweenDirections.actionLockSettingBetweenFragmentToLockSettingTargetFragment()
            navController.navigate(action)
        }
    }


}