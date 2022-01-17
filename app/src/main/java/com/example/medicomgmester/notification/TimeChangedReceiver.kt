package com.example.medicomgmester.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.TIME_SET") {
            /*ideally we should be fetching the data from a database*/
            val sharedPref = context?.getSharedPreferences("MyPref",Context.MODE_PRIVATE) ?: return
            val timeInMilliInTwoDay = sharedPref.getLong("timeInMilliInTwoDay", 1)
            val timeInMilliInSevenDay = sharedPref.getLong("timeInMilliInSevenDay", 1)
            val timeInMilliOutTwoDay = sharedPref.getLong("timeInMilliOutTwoDay", 1)
            val timeInMilliOutSevenDay = sharedPref.getLong("timeInMilliOutSevenDay", 1)
            Utils.inTwoDayAdvanceNotice(context, timeInMilliInTwoDay)
            Utils.inSevenDayAdvanceNotice(context, timeInMilliInSevenDay)
            Utils.outTwoDayAdvanceNotice(context, timeInMilliOutTwoDay)
            Utils.outSevenDayAdvanceNotice(context, timeInMilliOutSevenDay)
        }
    }
}