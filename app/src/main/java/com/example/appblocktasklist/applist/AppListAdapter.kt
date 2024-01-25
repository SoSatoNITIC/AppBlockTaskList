package com.example.appblocktasklist.applist


import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appblocktasklist.R

class AppListAdapter(context: Context, private val appInfo: List<ApplicationInfo>) :
    ArrayAdapter<ApplicationInfo>(context, R.layout.app_list_item, appInfo) {
    private val pm: PackageManager = context.packageManager
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val info = appInfo[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.app_list_item, parent, false)

        val iconView = view.findViewById<ImageView>(R.id.app_icon)
        val nameView = view.findViewById<TextView>(R.id.app_name)

        iconView.setImageDrawable(info.loadIcon(pm))
        nameView.text = info.loadLabel(pm).toString()

        return view
    }
}


