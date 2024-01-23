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
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

import java.util.Calendar


class SystemAdvancedSetting : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()


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

        val notificationTimings: ArrayList<String> = ArrayList()
        val notificationTimingList: ListView = view.findViewById(R.id.notificationTimingList)



        //sharedViewModel.preNoticeTiming.observe(viewLifecycleOwner, Observer { preNoticeTimings ->
        //    // preNoticeTimingsが更新されたときの処理を書く
        //    // 例えば、preNoticeTimingsをログに出力する
        //    println(preNoticeTimings)
        //    // preNoticeTimingsから時間と分を取得し、指定した形式に変換し、リストに追加
        //    preNoticeTimings.forEach { duration ->
        //        val hours = duration.toHoursPart()
        //        val minutes = duration.toMinutesPart() % 60
        //        val displayTime = String.format("%d時間%d分前", hours, minutes)
        //        notificationTimings.add(displayTime)
        //    }
        //    // ArrayAdapterを使用してListViewを更新
        //    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
        //    notificationTimingList.adapter = adapter
        //})

        //上のやつでもいいけど画面遷移時の挙動がキモイからちょっと待つ
        sharedViewModel.preNoticeTiming.observe(viewLifecycleOwner, Observer { preNoticeTimings ->
            // preNoticeTimingsが更新されたときの処理を書く
            // 例えば、preNoticeTimingsをログに出力する
            println(preNoticeTimings)

            // preNoticeTimingsから時間と分を取得し、指定した形式に変換し、リストに追加
            preNoticeTimings.forEach { duration ->
                val hours = duration.toHoursPart()//バージョン差分で赤線出るかも
                val minutes = duration.toMinutesPart() % 60
                val displayTime = String.format("%d時間%d分前", hours, minutes)
                notificationTimings.add(displayTime)
            }


            val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


            // 画面遷移後にListViewを更新
            coroutineScope.launch {
                delay(100L) // 0.1秒待機
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
                notificationTimingList.adapter = adapter
            }
        })







        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
        notificationTimingList.adapter = adapter





        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController



        val notificationTimingButton: Button = view.findViewById(R.id.notificationTimingButton)
        notificationTimingButton.setOnClickListener {


            // ユーザーが設定した時間を返す新しいTimePickerDialogのインスタンスを作成
            val timePickerDialog = TimePickerDialog(requireContext(),
                { _, hourOfDay, minute ->
                    if(hourOfDay!=0 || minute !=0){
                        // 選択した時間をArrayListに追加
                        val displayTime = String.format("%d時間%d分前", hourOfDay, minute)
                        //タイミングが重複してなかったら追加
                        if (!notificationTimings.contains(displayTime)) {
                            notificationTimings.add(displayTime)
                            // ArrayAdapterを使用してListViewを更新
                            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notificationTimings)
                            notificationTimingList.adapter = adapter
                        }
                    }else{
                        Toast.makeText(requireContext(), "0分以上の時間を指定してください", Toast.LENGTH_SHORT).show()
                    }

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


            val preNoticeTimings: List<Duration> = notificationTimings.map { timeStr ->
                val parts = timeStr.split("時間")
                val partsMinutes = parts.last().split("分前")
                val hours = parts.first().toLong()
                val minutes = partsMinutes.first().toLong()
                Duration.ofHours(hours).plusMinutes(minutes)
            }



            // preNoticeTimingsが空である場合、トーストメッセージを表示します。
            if (preNoticeTimings.isEmpty()) {
                Toast.makeText(requireContext(), "通知タイミングが設定されていません", Toast.LENGTH_SHORT).show()
            } else {
                println(preNoticeTimings)
                sharedViewModel.setPreNoticeTiming(preNoticeTimings)

                val action = SystemAdvancedSettingDirections.actionSystemAdvancedSettingFragmentToLockSettingDetailsFragment()
                navController.navigate(action)
            }
        }

    }


}