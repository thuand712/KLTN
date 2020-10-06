package ducthuan.com.lamdep.ObjectClass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.R;

//nhan intent tu he thong hoac trao doi cac ung dung voi nhau
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Ringtone ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        ringtone.play();

        Intent mainIntent = new Intent(context, TrangChuActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,mainIntent,0);



        String tenbaiviet = intent.getStringExtra("tenbaiviet");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setSmallIcon(R.drawable.ic_dongho_black_24dp)
                .setContentTitle("Nhắc nhở từ Beauty")
                .setContentText(tenbaiviet)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());



    }

}
