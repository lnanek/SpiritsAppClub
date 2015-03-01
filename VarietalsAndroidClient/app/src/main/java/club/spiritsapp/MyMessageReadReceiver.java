package club.spiritsapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lnanek on 3/1/15.
 */
public class MyMessageReadReceiver extends BroadcastReceiver {

    private static final String TAG = MyMessageReadReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive intent = " + intent);


        // If you set up the intent as described in
        // "Create conversation read and reply intents",
        // you can get the conversation ID by calling:
        int conversationId = intent.getIntExtra("conversation_id", -1);

        // Remove the notification to indicate it has been read
        // and update the list of unread conversations in your app.

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(SendCarScheduleChange.SCHEDULE_CHANGE_NOTIFICATION_ID);
    }
}
