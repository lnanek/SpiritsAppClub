package club.spiritsapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lnanek on 3/1/15.
 */
public class MyMessageReplyReceiver extends BroadcastReceiver {

    private static final String TAG = MyMessageReadReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive intent = " + intent);
        // If you set up the intent as described in
        // "Create conversation read and reply intents",
        // you can get the conversation ID by calling:
        int conversationId = intent.getIntExtra("conversation_id", -1);

                final CharSequence userSpokenText = getMessageText(intent);
        Log.i(TAG, "onReceive userSpokenText = " + userSpokenText);

        if (userSpokenText.toString().equalsIgnoreCase("YES")) {
            final VarietalsApp app = VarietalsApp.instance;
            app.prefs.setChosenVarietals(new HashSet<String>());
            app.prefs.setChosenTypes(new HashSet<String>());
            app.prefs.setNextVineyard(null);
        }

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    /**
     * Get the message text from the intent.
     * Note that you should call
     * RemoteInput.getResultsFromIntent() to process
     * the RemoteInput.
     */
    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput =
                RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence("extra_voice_reply");
        }
        return null;
    }


}