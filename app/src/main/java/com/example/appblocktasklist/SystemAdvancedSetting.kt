package com.example.appblocktasklist

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.fragment.NavHostFragment
import android.app.AlertDialog

import java.util.Calendar


class SystemAdvancedSetting : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_system_advanced_setting_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val notificationTimingList: ListView = view.findViewById(R.id.notificationTimingList)
        val notificationTimings: ArrayList<String> = ArrayList()


        val notificationTimingButton: Button = view.findViewById(R.id.notificationTimingButton)
        notificationTimingButton.setOnClickListener {


            // ユーザーが設定した時間を返す新しいTimePickerDialogのインスタンスを作成
            val timePickerDialog = TimePickerDialog(requireContext(),
                { _, hourOfDay, minute ->
                    // 選択した時間をArrayListに追加
                    val displayTime = String.format("%d時間%d分前", hourOfDay, minute)
                    notificationTimings.add(displayTime)
                    // ArrayAdapterを使用してListViewを更新
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
                    notificationTimingList.adapter = adapter
                }, 0, 0, true)
            timePickerDialog.show()
        }

        notificationTimingList.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(requireContext()).apply {
                setTitle("削除確認")
                setMessage("この通知タイミングを削除しますか？")
                setPositiveButton("OK") { _, _ ->
                    notificationTimings.removeAt(position)
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
                    notificationTimingList.adapter = adapter
                }
                setNegativeButton("キャンセル", null)
                create().show()
            }
            true
        }



        view.findViewById<Button>(R.id.systemAdvancedSettingNextBottun).setOnClickListener{
            val action = SystemAdvancedSettingDirections.actionSystemAdvancedSettingFragmentToLockSettingDetailsFragment()
            navController.navigate(action)
        }

    }


}