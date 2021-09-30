package uz.techie.shifobaxshasaluz.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.MainActivity;

public class MessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_PRIMARY = "channel_fcm_messagin";
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        createNotificationChannel();


        if (remoteMessage.getNotification() != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d("TAG", "Message Notification Title: " + remoteMessage.getNotification().getTitle());

            String message = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            createNotification(title, message);
        }
    }

    private void createNotificationChannel() {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_PRIMARY,
                    "channel_FCM_Messaging",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("app primary channel");
            notificationManager.createNotificationChannel(channel);
        }

    }


    private void createNotification(String title, String message){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                0
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_PRIMARY)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setColor(getResources().getColor(R.color.purple_500))
                .setContentIntent(pendingIntent);


        notificationManager.notify(111, builder.build());
    }

}
