package com.juanarton.githubapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.juanarton.githubapp.alarm.AlarmReminder
import com.juanarton.githubapp.databinding.ActivityNotificationSettingBinding
import java.util.*

class NotificationSettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReminder
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var binding: ActivityNotificationSettingBinding

    companion object {
        const val PREFS_NAME = "SwitchStat"
        private const val SPREF_KEY = "daily"
        const val ALARM_ID = 102
        private const val TIME = "09:00"

        fun setRepeatingAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReminder::class.java)
            val timeArray = TIME.split(":").toTypedArray()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            calendar.set(Calendar.SECOND, 0)
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }else{
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReminder()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        supportActionBar?.title = getString(R.string.notifsetting)

        setSwitch()
        binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setRepeatingAlarm(this)
                notifon()
            } else {
                cancelAlarm()
                noTifoff()
            }
            saveChange(isChecked)
        }
    }

    private fun cancelAlarm() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReminder::class.java)
        val requestCode = ALARM_ID
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun setSwitch() {
        val stat = mSharedPreferences.getBoolean(SPREF_KEY, false)
        binding.switchAlarm.isChecked = stat
        if(stat){
            notifon()
        }else{
            noTifoff()
        }
    }

    private fun notifon(){
        binding.bellIcon.setImageResource(R.drawable.ic_baseline_notifications_active_24)
        binding.notifstat.text = getString(R.string.notifstaton)
    }

    private fun noTifoff(){
        binding.bellIcon.setImageResource(R.drawable.ic_baseline_notifications_off_24)
        binding.notifstat.text = getString(R.string.notifstatoff)
    }

    private fun saveChange(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(SPREF_KEY, value)
        editor.apply()
    }
}