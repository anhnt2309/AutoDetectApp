package com.example.admin.autodetectapp;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.deps.guava.eventbus.EventBus;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 12/31/16.
 */

public class DetectService extends Service {
    NotificationManager notificationManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private  Timer timer;

    private static final String TAG = "DetectService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Create Service from service class");
        super.onCreate();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();

        // Prepare intent which is triggered if the
        // notification is selected
        Intent NotiIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), NotiIntent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("AUTO DETECT SERVICE")
                .setContentText("AutoDetect Service Started")
                .setSmallIcon(R.drawable.radar).getNotification();
//                .setContentIntent(pIntent).getNotification();

        //start notification manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        // hide the notification after its selected
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        // get info from activity
//        final String name = intent.getStringExtra("name");
        final String name = "com.google.android.apps.maps";
        int time = intent.getIntExtra("time", 1);

        // begin to detect app
        final String str = "";


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {


            int flag = 0;

            @Override
            public void run() {
                LocationListener locationListener = new MyLocationListener(getApplicationContext());

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.e("Latitude",""+location.getLatitude());
                Log.e("Longtitude",""+location.getLongitude());
                String copyString = ""+location.getLatitude()+location.getLongitude();



                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> runningAppProcessInfo = am.getRunningServices(Integer.MAX_VALUE);
                Log.d("size", "" + runningAppProcessInfo.size());
                for (ActivityManager.RunningServiceInfo appProcess : runningAppProcessInfo) {
                    if (appProcess.process.equals(name) == true) {
                        flag = 1;
                        if (appProcess.foreground == false) {
                            Log.d(str, name + "has been launched");
                        }
                    } else {
                        flag = 0;
                    }
                }
                if (flag != 1) {
                    Log.d(str, name + " not running");
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location.getLatitude()+","+location.getLongitude());

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mapIntent);
                    }
//                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(name);
//                    if (launchIntent != null) {
//
//                        startActivity(launchIntent);//null pointer check in case package name was not found
//                    }
                }
            }
        }, 0, ( 1*60*1000));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
        timer.cancel();
        Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Stop Service");
    }


}
