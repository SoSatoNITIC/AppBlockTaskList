package com.example.appblocktasklist



import android.app.AlertDialog
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter



class LockSettingTarget : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_target_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val packageManager: PackageManager = requireContext().packageManager

        val appListView = view.findViewById<ListView>(R.id.app_list)
        val appListViewChoose = view.findViewById<ListView>(R.id.choose_appList)

        // システムアプリを除外する Youtube関係は保持
        val installedApps = ArrayList<ApplicationInfo>(getInstalledNotSystemApp())
        // appListViewChooseのデータを保持するためのArrayListを作成
        val chosenApps = ArrayList<ApplicationInfo>()

        // アダプター作成
        val installedAppsAdapter = AppListAdapter(requireActivity(), installedApps)
        val chosenAppsAdapter = AppListAdapter(requireActivity(), chosenApps)

        //app の一覧表示
        appListView.adapter = installedAppsAdapter
        appListViewChoose.adapter = chosenAppsAdapter

        sharedViewModel.targetApp.observe(viewLifecycleOwner) { apps ->
            // appsは更新された値
            // ここでappsを使ってUIを更新する
            chosenApps.clear()
            chosenApps.addAll(apps.map { packageManager.getApplicationInfo(it, PackageManager.GET_META_DATA) })

            chosenAppsAdapter.notifyDataSetChanged()

            println(appListViewChoose)
        }

        //文字表示のみはこれでできた
        appListView.setOnItemClickListener { parent, view, position, id ->
            // 選択された要素を取得します。
            val appInfo = installedApps[position]

            // 選択された要素が既にArrayListに存在するかどうかを確認します。
            if (!chosenApps.contains(appInfo)) {
//                val appName = appInfo.loadLabel(packageManager)
                chosenApps.add(appInfo)
                chosenAppsAdapter.notifyDataSetChanged()

            } else {
                Toast.makeText(requireActivity(), "すでに選択されています", Toast.LENGTH_SHORT).show()
            }
        }


        appListViewChoose.setOnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(requireActivity())
                .setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->

                    if (position >= 0 && position < chosenApps.size) {
                        chosenApps.removeAt(position)
                    }

                    // update ListView
                    chosenAppsAdapter.notifyDataSetChanged()
                    //Toast.makeText(requireActivity(), "削除しました", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("キャンセル", null)
                .setCancelable(true)
                .show()
            true
        }



        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            if (appListViewChoose.isEmpty()) {
                Toast.makeText(requireActivity(), "アプリが選択されていません", Toast.LENGTH_SHORT).show()
            } else {
                println(appListViewChoose)

                //重複判定　　もっとうまい方法あるはず...
                val uniqueListData = chosenApps.distinct()

                val packageNames = uniqueListData.map { it.packageName }

                sharedViewModel.setTargetApp(packageNames)

                val action = LockSettingTargetDirections.actionLockSettingTargetFragmentToSystemAdvancedSettingFragment()
                navController.navigate(action)
            }
        }
    }

    private fun getInstalledNotSystemApp(): List<ApplicationInfo> {
        val packageManager: PackageManager = requireActivity().packageManager

        val installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return installedApplications.filter { appInfo ->
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    appInfo.packageName == "com.google.android.youtube" ||
                    appInfo.packageName == "com.google.android.apps.youtube.music" ||
                    appInfo.packageName == "com.android.chrome"
        }
    }
}