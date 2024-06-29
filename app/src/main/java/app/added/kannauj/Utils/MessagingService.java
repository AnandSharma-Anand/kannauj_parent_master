package app.added.kannauj.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class MessagingService extends Service {

    private LocalBroadcastManager broadcast;
    Timer timer = new Timer();
    TimerTask updateChat = new CustomTimerTask(MessagingService.this);

    public MessagingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
//        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        timer.scheduleAtFixedRate(updateChat, 0, 2000);
        broadcast = LocalBroadcastManager.getInstance(this);
    }

    public void sendResult(String response) {
        Intent intent = new Intent("CHAT");
        if (response != null) {
            intent.putExtra("val", response);
            broadcast.sendBroadcast(intent);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
//        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        timer.cancel();
    }

    private class CustomTimerTask extends TimerTask {
        private Context context;
        private Handler mHandler = new Handler();

        CustomTimerTask(Context con) {
            this.context = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {

                public void run() {
                    mHandler.post(new Runnable() {
                        public void run() {
//                            Toast.makeText(context, "DISPLAY YOUR MESSAGE", Toast.LENGTH_SHORT).show();
                            sendResult("tick");
                        }
                    });
                }
            }).start();
        }
    }
}