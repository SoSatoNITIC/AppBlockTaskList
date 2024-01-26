package com.example.appblocktasklist.applist


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appblocktasklist.R

class AppListAdapter(context: Context, private val appIcons: List<Drawable>, private val appNames: List<String>) :
    ArrayAdapter<String>(context, R.layout.app_list_item, appNames) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.app_list_item, parent, false)

        val iconView = view.findViewById<ImageView>(R.id.app_icon)
        val nameView = view.findViewById<TextView>(R.id.app_name)

        iconView.setImageDrawable(appIcons[position])
        nameView.text = appNames[position]

        return view
    }
}


