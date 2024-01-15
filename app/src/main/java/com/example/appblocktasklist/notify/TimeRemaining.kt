package com.example.appblocktasklist.notify

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import android.widget.Toast
import com.example.appblocktasklist.R

class TimeRemaining {
    fun sendRemainingTimeNotification(context: Context, remainingTime: Int) {
        // Check if the app has POST_NOTIFICATIONS permission
        //パーミッションとれてるか確認　とれてなかったらとってもらうように画面遷移
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Show a dialog explaining why the permission is needed
            AlertDialog.Builder(context)
                .setMessage("このアプリが正しく機能するにはPOST_NOTIFICATIONSパーミッションが必要です。")
                .setPositiveButton("OK") { _, _ ->
                    // Redirect user to the settings screen where they can grant the permission
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
            return
        }


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android Oreo and above, we need to create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer"
            val descriptionText = "Notifications for timer"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("timer", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                lightColor = Color.RED
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, "timer")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("残り時間")
            .setContentText("残り$remainingTime 分です")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(notificationManager) {
            notify(0, builder.build())
        }
    }
}
