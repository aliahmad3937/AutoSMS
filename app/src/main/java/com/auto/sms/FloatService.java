package com.auto.sms;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.auto.sms.models.MissedCallModel;
import com.auto.sms.utils.SharePrefs;
import com.google.gson.Gson;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FloatService extends Service {
    AppChecker appChecker;
    private static final int timeout = 61000;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "autosms";
    private static final String CHANNEL_ID = "13";
    private final String TAG = "FloatService";
    private static final int ONGOING_NOTIFICATION_ID = 14;


    @Override
    public void onCreate() {
        super.onCreate();

        appChecker = new AppChecker();
    }

    @Override
    public void onDestroy() {

//        appChecker.stop();
//        SavedPreference.clearPreferences(getApplication());
        Log.e("TAG6", "Service is going to destro!");
        super.onDestroy();
        //  appChecker.stop();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "AutoSms notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DEFAULT_IMPORTANCE, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        createNotificationChannel();

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Auto Sms")
                        .setContentText("Auto Sms is running...")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        //    .setContentIntent(pendingIntent)
                        .setTicker("Auto Sms is running")
                        .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startForeground(ONGOING_NOTIFICATION_ID, notification);
        } else {
            startService(intent);
        }

//        appChecker.whenAny(new AppChecker.Listener() {
//            @Override
//            public void onForeground(String packageName) {
//                ArrayList inContactList = SharePrefs.getInstance(getApplication()).getInContactList(getApplication());
//                ArrayList outContactList = SharePrefs.getInstance(getApplication()).getOutContactList(getApplication());
//                int afterCount = SharePrefs.getInstance(getApplication()).getAfterCount(getApplication());
//
//                MissedCallModel missedCallModel = SharePrefs.getInstance(getApplication()).getMissedCallObject(getApplication());
//                Map<String, String> missCallMap = SharePrefs.getInstance(getApplication()).getMissCallMap(getApplication());
//                Map<String, String> receiveCallMap = SharePrefs.getInstance(getApplication()).getReceiveCallMap(getApplication());
//                Calendar cl = Calendar.getInstance();
//                cl.setTimeInMillis(System.currentTimeMillis());
//                int hours = cl.get(Calendar.HOUR_OF_DAY);
//                int minutes = cl.get(Calendar.MINUTE);
//
//                if (missedCallModel != null) {
//                    if (afterCount != 0) {
//                        //    Log.i("TAG", "missed call number :" + number);
//                        if (hours == missedCallModel.getHours() && minutes == missedCallModel.getMinute()) {
//                            if (!missCallMap.isEmpty()) {
//
//                                for (String keys : missCallMap.keySet()) {
//                                    MissedCallModel model = new Gson().fromJson(missCallMap.get(keys), MissedCallModel.class);
//                                    if (model.getAfterCount() < afterCount) {
//                                        model.setAfterCount(model.getAfterCount() + 1);
//                                        missCallMap.put(model.getNumbers(), new Gson().toJson(model));
//
//                                    } else {
//                                        if (model.getCount() < model.getDays()) {
//                                            if ((!inContactList.isEmpty() && inContactList.contains(model.getNumbers())) || (!outContactList.isEmpty() && outContactList.contains(model.getNumbers()))) {
//                                                // do nothing
//                                            } else {
//
//                                                sendSmsMsgFnc(model.getNumbers(), missedCallModel.getSms(), getApplication());
//                                              //  Log.v("TAG", "missedCallModel Send Msg now....:day:" + missedCallModel.getDays() + " count:" + missedCallModel.getCount() + " number :" + number);
//                                                model.setCount(missedCallModel.getCount() + 1);
//                                                model.setSms(missedCallModel.getSms());
//                                                model.setDays(missedCallModel.getDays());
//                                                model.setHours(missedCallModel.getHours());
//                                                model.setMinute(missedCallModel.getMinute());
//
//                                                missCallMap.put(model.getNumbers(),new Gson().toJson(model));
//                                            }
//
//                                        }
//                                    }
//                                }
//                                SharePrefs.getInstance(getApplication()).
//                                        saveMissCallMap(getApplication(), missCallMap);
//                            }
//                        }
//                    }
//                }
//
//
//
//
//        MissedCallModel receivedCallModel = SharePrefs.getInstance(getApplication()).getReceivedCallObject(getApplication());
//
//                if (receivedCallModel != null) {
//                    if (afterCount != 0) {
//                        //    Log.i("TAG", "missed call number :" + number);
//                        if (hours == receivedCallModel.getHours() && minutes == receivedCallModel.getMinute()) {
//                            if (!receiveCallMap.isEmpty()) {
//
//                                for (String keys : receiveCallMap.keySet()) {
//                                    MissedCallModel model = new Gson().fromJson(receiveCallMap.get(keys), MissedCallModel.class);
//                                    if (model.getAfterCount() < afterCount) {
//                                        model.setAfterCount(model.getAfterCount() + 1);
//                                        receiveCallMap.put(model.getNumbers(), new Gson().toJson(model));
//
//                                    } else {
//                                        if (model.getCount() < model.getDays()) {
//                                            if ((!inContactList.isEmpty() && inContactList.contains(model.getNumbers())) || (!outContactList.isEmpty() && outContactList.contains(model.getNumbers()))) {
//                                                // do nothing
//                                            } else {
//
//                                                sendSmsMsgFnc(model.getNumbers(), receivedCallModel.getSms(), getApplication());
//                                                //  Log.v("TAG", "missedCallModel Send Msg now....:day:" + missedCallModel.getDays() + " count:" + missedCallModel.getCount() + " number :" + number);
//                                                model.setCount(missedCallModel.getCount() + 1);
//                                                model.setSms(missedCallModel.getSms());
//                                                model.setDays(missedCallModel.getDays());
//                                                model.setHours(missedCallModel.getHours());
//                                                model.setMinute(missedCallModel.getMinute());
//
//                                                receiveCallMap.put(model.getNumbers(),new Gson().toJson(model));
//                                            }
//
//                                        }
//                                    }
//                                }
//                                SharePrefs.getInstance(getApplication()).
//                                        saveReceiveCallMap(getApplication(), receiveCallMap);
//                            }
//                        }
//                    }
//                }
//
//        MissedCallModel sveContactModel = SharePrefs.getInstance(getApplication()).getContactsObject(getApplication());
//        if (sveContactModel != null) {
//
//            if (hours == sveContactModel.getHours() && minutes == sveContactModel.getMinute()) {
//                if (sveContactModel.getCount() < sveContactModel.getDays()) {
//                    String[] contacts = sveContactModel.getNumbers().split(",");
//                    for (String contact : contacts) {
//                        sendSmsMsgFnc(contact, sveContactModel.getSms(), getApplication());
//                    }
//                    Log.v("TAG", "savedContactCallModel Send Msg now....:day:" + sveContactModel.getDays() + " count:" + sveContactModel.getCount() + "numbers :" + sveContactModel.getNumbers());
//                    sveContactModel.setCount(sveContactModel.getCount() + 1);
//                    SharePrefs.getInstance(getApplication()).saveContactsObject(
//                            getApplication(),
//                            new MissedCallModel(
//                                    sveContactModel.getSms(),
//                                    sveContactModel.getNumbers(),
//                                    sveContactModel.getDays(),
//                                    sveContactModel.getCount(),
//                                    0,
//                                    sveContactModel.getHours(),
//                                    sveContactModel.getMinute()
//                            )
//                    );
//                } else {
//                    Log.v("TAG", "Save Comtact Send time is over!");
//                }
//
//            } else {
//                Log.v("TAG", "Save Comtact Msg time is not match::");
//            }
//        } else {
//            Log.v("TAG", "sveContactModel Send Msg is null::");
//        }
//
//
//        MissedCallModel missContactModel = SharePrefs.getInstance(getApplication()).getMissContactsObject(getApplication());
//        if (missContactModel != null) {
//
//            if (hours == missContactModel.getHours() && minutes == missContactModel.getMinute()) {
//                if (missContactModel.getCount() < missContactModel.getDays()) {
//                    String[] contacts = missContactModel.getNumbers().split(",");
//                    for (String contact : contacts) {
//                        sendSmsMsgFnc(contact, missContactModel.getSms(), getApplication());
//                    }
//                    Log.v("TAG", "missContactModel Send Msg now....:day:" + missContactModel.getDays() + " count:" + missContactModel.getCount() + "numbers :" + missContactModel.getNumbers());
//                    missContactModel.setCount(missContactModel.getCount() + 1);
//                    SharePrefs.getInstance(getApplication()).saveMissContactsObject(
//                            getApplication(),
//                            new MissedCallModel(
//                                    missContactModel.getSms(),
//                                    missContactModel.getNumbers(),
//                                    missContactModel.getDays(),
//                                    missContactModel.getCount(),
//                                    0,
//                                    missContactModel.getHours(),
//                                    missContactModel.getMinute()
//                            )
//                    );
//                } else {
//                    Log.v("TAG", "missContactModel Send time is over!");
//                }
//
//            } else {
//                Log.v("TAG", "missContactModel Msg time is not match::");
//            }
//        } else {
//            Log.v("TAG", "missContactModel Send Msg is null::");
//        }
//    }
//})
//        .timeout(timeout)
//        .start(this);


        return START_STICKY;
        }

public void composeSMSMessage(String msg,String numbers){
        Intent intent=new Intent("android.intent.action.SENDTO");
        StringBuilder sb=new StringBuilder();
        sb.append("smsto:");
        sb.append(numbers);
        intent.setData(Uri.parse(sb.toString()));
        intent.putExtra("sms_body",msg);
        if(intent.resolveActivity(getPackageManager())!=null){
        startActivity(intent);
        }
        }

public void sendSmsMsgFnc(String mblNumVar,String smsMsgVar,Context context){
        Log.i("sendSmsMsgFnc","sendSmsMsgFnc: nmbr:");

        try{
        SmsManager smsMgrVar=SmsManager.getDefault();
        smsMgrVar.sendTextMessage(mblNumVar,null,smsMsgVar,null,null);
        Toast.makeText(
        context,context.getString(R.string.sent_sms),
        Toast.LENGTH_LONG
        ).show();
        }catch(Exception ErrVar){
        Log.i("TAG","sendSmsMsgFnc: nmbr: error:"+ErrVar.getMessage());

        Toast.makeText(
        context,ErrVar.getMessage().toString(),
        Toast.LENGTH_LONG
        ).show();
        ErrVar.printStackTrace();
        }

        }


        }
