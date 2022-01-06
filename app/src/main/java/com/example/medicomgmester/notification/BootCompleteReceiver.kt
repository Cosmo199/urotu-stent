package com.example.medicomgmester.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            /*ideally we should be fetching the data from a database*/
            val sharedPref = context?.getSharedPreferences("MyPref", Context.MODE_PRIVATE) ?: return
            val timeInMilli = sharedPref.getLong("timeInMilli", 1)
            val timeInMilli2 = sharedPref.getLong("timeInMilli2", 1)
            Utils.inTwoDayAdvanceNotice(context, timeInMilli)
            //Utils.inSevenDayAdvanceNotice(context, timeInMilli)
            Utils.outTwoDayAdvanceNotice(context, timeInMilli2)
            //Utils.outSevenDayAdvanceNotice(context, timeInMilli2)
        }
    }
}