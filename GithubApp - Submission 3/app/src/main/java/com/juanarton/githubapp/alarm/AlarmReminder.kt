package com.juanarton.githubapp.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.juanarton.githubapp.BuildConfig
import com.juanarton.githubapp.NotificationSettingActivity.Companion.setRepeatingAlarm
import com.juanarton.githubapp.R
import com.juanarton.githubapp.SplashScreenActivity


class AlarmReminder : BroadcastReceiver() {

    companion object{
        const val TITLE = "GithubApp"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = BuildConfig.MESSAGE
        showAlarmNotification(context, TITLE, message)
        setRepeatingAlarm(context)
    }

    private fun showAlarmNotification(context: Context, title: String, message: String) {
        val channelId = "GithubApp"
        val channelName = "Daily Reminder"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, SplashScreenActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(500, 0, 500))
            .setSound(notifSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(500, 0, 500)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(100, notification)
    }
}