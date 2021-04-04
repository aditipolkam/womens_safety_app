package com.example.womensafety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context,RecieveCall.class);
        context.startActivity(intent1);
    }
}
