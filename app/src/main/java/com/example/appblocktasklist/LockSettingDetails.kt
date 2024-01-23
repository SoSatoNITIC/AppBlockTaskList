package com.example.appblocktasklist

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter


class LockSettingDetails : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()

    // chosenAppIconsとchosenAppNamesをフラグメントのプロパティとして定義
    private val chosenAppIcons = ArrayList<Drawable>()
    private val chosenAppNames = ArrayList<String>()



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

        val appListViewChoose = view.findViewById<ListView>(R.id.choosed_App)

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

        sharedViewModel.targetApp.observe(viewLifecycleOwner, Observer { apps ->
            // appsは更新された値
            // ここでappsを使ってUIを更新する

            // 選択されたアプリの名前とアイコンを取得
            val appNames = apps.map { appName -> appName }
            val appIcons = apps.map { appName -> appKey[appName] ?: ColorDrawable() }


            // chosenAppNamesとchosenAppIconsを更新
            chosenAppNames.clear()
            chosenAppNames.addAll(appNames)
            chosenAppIcons.clear()
            chosenAppIcons.addAll(appIcons)

            // ListViewのアダプターを更新
            val adapterChoose = AppListAdapter(requireActivity(), chosenAppIcons, chosenAppNames)
            appListViewChoose.adapter = adapterChoose


            println(appListViewChoose)
        })



















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