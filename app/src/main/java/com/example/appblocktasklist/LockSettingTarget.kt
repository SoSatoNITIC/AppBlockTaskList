package com.example.appblocktasklist



import android.app.AlertDialog
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
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
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.appblocktasklist.applist.AppListAdapter



class LockSettingTarget : Fragment() {
    private val sharedViewModel: LockViewModel by activityViewModels()


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


        val appListViewChoose = view.findViewById<ListView>(R.id.choose_appList)

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


        //val packageManager: PackageManager = requireActivity().packageManager

        //val installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // システムアプリを除外する Youtube関係は保持
        //val userApps = installedApplications.filter { appInfo ->
        //    (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
        //            appInfo.packageName == "com.google.android.youtube" ||
        //            appInfo.packageName == "com.google.android.apps.youtube.music" ||
        //            appInfo.packageName == "com.android.chrome"
        //}
        //AppNameとIcon表示
        //val appNames = userApps.map { it.loadLabel(packageManager).toString() }

        //val appIcons = userApps.map { it.loadIcon(packageManager) }


        //val appKey = appNames.zip(appIcons).toMap()

        val adapter = AppListAdapter(requireActivity(), appIcons, appNames)

        //app の一覧表示
        val appListView = view.findViewById<ListView>(R.id.app_list)
        appListView.adapter = adapter



        //val appListViewChoose = view.findViewById<ListView>(R.id.choose_appList)
        // appListViewChooseのデータを保持するためのArrayListを作成
        val chosenApps = ArrayList<String>()

        //文字表示のみはこれでできた
        appListView.setOnItemClickListener { parent, view, position, id ->
            // 選択された要素を取得します。
            val selectedItem = parent.getItemAtPosition(position).toString()


            // 選択された要素が既にArrayListに存在するかどうかを確認します。
            if (!chosenApps.contains(selectedItem.toString())) {


                chosenAppIcons.add(0,appKey[selectedItem]!!)
                chosenAppNames.add(0,selectedItem)


                // ArrayAdapterを更新します。
                val adapterChoose = AppListAdapter(requireActivity(), chosenAppIcons, chosenAppNames)
                appListViewChoose.adapter = adapterChoose

                //保持リストに追加
                chosenApps.add(selectedItem)



            }else{
                Toast.makeText(requireActivity(), "すでに選択されています", Toast.LENGTH_SHORT).show()
            }
        }


        appListViewChoose.setOnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(requireActivity())
                .setMessage("削除しますか？")
                .setPositiveButton("OK") { dialog, which ->


                    // remove item from ArrayList
                    //chosenAppIcons.removeAt(position)
                    //chosenAppNames.removeAt(position)
                    //保持リストからも削除
                    //chosenApps.removeAt(position)

                    if (position >= 0 && position < chosenAppIcons.size) {
                        chosenAppIcons.removeAt(position)
                    }
                    if (position >= 0 && position < chosenAppNames.size) {
                        chosenAppNames.removeAt(position)
                    }
                    if (position >= 0 && position < chosenApps.size) {
                        chosenApps.removeAt(position)
                    }



                    // update ListView
                    val adapterChoose = AppListAdapter(requireActivity(), chosenAppIcons, chosenAppNames)
                    appListViewChoose.adapter = adapterChoose
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

                val adapter = appListViewChoose.adapter as ArrayAdapter<String>
                val listData = mutableListOf<String>()
                for (i in 0 until adapter.count) {
                    val item = adapter.getItem(i)
                    if (item != null) {
                        listData.add(item)
                    }
                }

                //重複判定　　もっとうまい方法あるはず...
                var setData = listData.toSet()
                var uniqueListData = setData.toList()


                if (uniqueListData.last() == ""){
                    sharedViewModel.setTargetApp(uniqueListData.slice(0 until uniqueListData.size - 1))//なんか後ろにキモイの入ってるから消す
                }else{
                    sharedViewModel.setTargetApp(uniqueListData)
                }



                val action = LockSettingTargetDirections.actionLockSettingTargetFragmentToSystemAdvancedSettingFragment()
                navController.navigate(action)
            }
        }
    }



}