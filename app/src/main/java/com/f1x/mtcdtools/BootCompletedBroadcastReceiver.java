package com.f1x.mtcdtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.service.MtcdService;

/**
 * Created by f1x on 2016-08-03.
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent startServiceIntent = new Intent(context, MtcdService.class);
            startServiceIntent.setAction(MtcdService.ACTION_AUTORUN);
            context.startService(startServiceIntent);
        }
    }
}