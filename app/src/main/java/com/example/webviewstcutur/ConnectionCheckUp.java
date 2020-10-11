package com.example.webviewstcutur;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class ConnectionCheckUp {

    private static final String TAG = "ConnectionCheckUp";
    public static volatile int NetConnected = 0;

    Context context;
    Switch switch_wifi;

    public ConnectionCheckUp(Context c) {
        this.context = c;
        this.switch_wifi = new Switch(context);
    }

    public int netStatus() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                //we have WIFI
                NetConnected = 2;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //we have cellular data
                NetConnected = 1;
            }
        } else {
            //we have no connection :(
            NetConnected = 0;
        }
        return NetConnected;

    }

    public  void NetErrorDialog() {

        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView textView = new TextView(context);
        textView.setText("Enable any ");

        switch_wifi.setText("Wifi ");
        switch_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    textView.setText("Wifi enabled");
                } else {
                    wifiManager.setWifiEnabled(false);
                }
            }
        });

        if (wifiManager.isWifiEnabled()) {
            switch_wifi.setChecked(true);
        } else {
            switch_wifi.setChecked(false);
        }


        Switch internet_switch = new Switch(context);
        internet_switch.setText("Internet ");
        internet_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setText("Internet  enabled");
                }
            }
        });
        textView.setText("Enable any ");

        layout.addView(textView);
        layout.addView(switch_wifi);
        layout.addView(internet_switch);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(layout)
                .setCancelable(true)
                .setNegativeButton("Quit App", null)
               ;
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button negBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                negBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToclose = false;
                        if(wantToclose)
                            dialog.dismiss();

                    }
                });
            }
        });
        dialog.show();

    }

    public  BroadcastReceiver wifistate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifistate  = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifistate){
                case WifiManager.WIFI_STATE_ENABLED:
                    switch_wifi.setChecked(true);
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    switch_wifi.setChecked(false);
                    break;
            }
        }
    };
}
