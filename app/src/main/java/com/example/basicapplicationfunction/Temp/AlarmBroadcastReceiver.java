package com.example.basicapplicationfunction.Temp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.basicapplicationfunction.R;

import java.util.Calendar;

import static com.example.basicapplicationfunction.Temp.App.CHANNEL_ID;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String TITLE = "TITLE";

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false)) {
                startAlarmService(context, intent);
            } {
                if (alarmIsToday(intent)) {
                    startAlarmService(context, intent);
                }
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MONDAY, false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUESDAY, false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THURSDAY, false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRIDAY, false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SATURDAY, false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUNDAY, false))
                    return true;
                return false;
        }
        return false;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My service channel";
            String description = "com.codechacha.sample1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("alarm_channel_id", "알람 테스트", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("알람테스트");
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            context.startService(intentService);
        }

        /*String channelId = "com.codechacha.sample1";
        String channelName = "My service channel";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        */
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }



}