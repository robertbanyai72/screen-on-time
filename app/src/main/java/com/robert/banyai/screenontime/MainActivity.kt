package com.robert.banyai.screenontime

import android.annotation.SuppressLint
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // showtime2()
        // getDefaultLauncherList()

        val usageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val appsMax = ArrayList<UsageStats>()

        val midnight =
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        var current = System.currentTimeMillis()
        // 10 min in millis
        val minutesToMillis = 600000L

        while (midnight < current) {
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                current.minus(minutesToMillis),
                current
            )

            stats.maxByOrNull { it.totalTimeInForeground }?.let { appsMax.add(it) }

            Log.d("end 1", "$current")
            current = current.minus(minutesToMillis)
            Log.d("end 2", "$current")
        }

        Log.d("result", "done")
    }

    @SuppressLint("NewApi")
    fun showtime2() {
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val stats = usageStatsManager.queryAndAggregateUsageStats(start, end)
    }

    // fun getDefaultLauncherList(): Array<String?>? {
    //     val intent = Intent(Intent.ACTION_MAIN)
    //     intent.addCategory(Intent.CATEGORY_HOME)
    //     val list = (packageManager as PackageManager).queryIntentActivities(intent, 0)
    //     val toReturn = arrayOfNulls<String>(list.size)
    //     for (i in list.indices) toReturn[i] = list[i].activityInfo.packageName
    //     return toReturn
    // }
}