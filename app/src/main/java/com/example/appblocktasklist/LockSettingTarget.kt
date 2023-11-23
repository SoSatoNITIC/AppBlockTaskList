package com.example.appblocktasklist

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter



class LockSettingTarget : Fragment() {


    // chosenAppIconsとchosenAppNamesをフラグメントのプロパティとして定義
    private val chosenAppIcons = ArrayList<Drawable>()
    private val chosenAppNames = ArrayList<String>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_setting_target_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val packageManager: PackageManager = requireActivity().packageManager

        val installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // システムアプリを除外する Youtube関係は保持
        val userApps = installedApplications.filter { appInfo ->
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    appInfo.packageName == "com.google.android.youtube" ||
                    appInfo.packageName == "com.google.android.apps.youtube.music" ||
                    appInfo.packageName == "com.android.chrome"
        }
        //AppNameとIcon表示
        val appNames = userApps.map { it.loadLabel(packageManager).toString() }

        val appIcons = userApps.map { it.loadIcon(packageManager) }


        val appKey = appNames.zip(appIcons).toMap()

        val adapter = AppListAdapter(requireActivity(), appIcons, appNames)

        //app の一覧表示
        val appListView = view.findViewById<ListView>(R.id.app_list)
        appListView.adapter = adapter



        val appListViewChoose = view.findViewById<ListView>(R.id.choose_appList)
        // appListViewChooseのデータを保持するためのArrayListを作成
        val chosenApps = ArrayList<String>()

        //文字表示のみはこれでできた
        appListView.setOnItemClickListener { parent, view, position, id ->
            // 選択された要素を取得します。
            val selectedItem = parent.getItemAtPosition(position).toString()


            // 選択された要素が既にArrayListに存在するかどうかを確認します。
            if (!chosenApps.contains(selectedItem.toString())) {


                chosenAppIcons.add(appKey[selectedItem]!!)
                chosenAppNames.add(selectedItem)


                // ArrayAdapterを更新します。
                val adapterChoose = AppListAdapter(requireActivity(), chosenAppIcons, chosenAppNames)
                appListViewChoose.adapter = adapterChoose
            }
        }


        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            val action = LockSettingTargetDirections.actionLockSettingTargetFragmentToSystemAdvancedSettingFragment()
            navController.navigate(action)
        }
    }
}