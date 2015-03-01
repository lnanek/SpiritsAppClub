package club.spiritsapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

/**
 * Created by lnanek on 3/1/15.
 */
public class SendCarScheduleChange {

    public static final String MESSAGE_HEARD_ACTION = "club.spiritsapp.ACTION_MESSAGE_HEARD";

    public static final String ACTION_MESSAGE_REPLY = "club.spiritsapp.ACTION_MESSAGE_REPLY";

    public static final String EXTRA_VOICE_REPLY = "voice_reply";

    public static final String PARTICIPANT_NAME = "Varietals.CLUB (Destination Closed)";

    public static final int SCHEDULE_CHANGE_CONVERSATION_ID = 1;

    public static final int SCHEDULE_CHANGE_NOTIFICATION_ID = 2;

    public static void send(final Context context) {

// Build a RemoteInput for receiving voice input in a Car Notification
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(context.getApplicationContext().getString(R.string.notification_reply))
                .build();


        final PendingIntent msgHeardPendingIntent = getMessageHeardIntent(context);
        final PendingIntent replyPendingIntent = getActionReplyIntent(context);


// Create an unread conversation object to organize a group of messages
// from a particular sender.
        NotificationCompat.CarExtender.UnreadConversation.Builder unreadConvBuilder =
                new NotificationCompat.CarExtender.UnreadConversation.Builder(PARTICIPANT_NAME)
                        .setReadPendingIntent(msgHeardPendingIntent)
                        .setReplyAction(replyPendingIntent, remoteInput);


        final long currentTimestamp = System.currentTimeMillis();
        unreadConvBuilder.addMessage("The scheduled Winery has run out of supplies.").setLatestTimestamp(currentTimestamp);

        unreadConvBuilder.addMessage("Cancel location? Reply Yes or No").setLatestTimestamp(currentTimestamp);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context.getApplicationContext())
                        .setSmallIcon(R.drawable.ic_logo)
                                //.setLargeIcon(context.getResources().getDrawable(R.mipmap.ic_launch))
                        .setContentText("Varietals.club schedule change")
                        .setWhen(currentTimestamp)
                        .setContentTitle(PARTICIPANT_NAME)
                        .setContentIntent(getMessageHeardIntent(context));

        notificationBuilder.extend(new NotificationCompat.CarExtender()
                .setUnreadConversation(unreadConvBuilder.build()));

        NotificationManagerCompat msgNotificationManager =
                NotificationManagerCompat.from(context);
        msgNotificationManager.notify(SCHEDULE_CHANGE_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent getMessageHeardIntent(final Context context) {
        Intent msgHeardIntent = new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(MESSAGE_HEARD_ACTION)
                .putExtra("conversation_id", SCHEDULE_CHANGE_CONVERSATION_ID);

        PendingIntent msgHeardPendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        SCHEDULE_CHANGE_CONVERSATION_ID,
                        msgHeardIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        return msgHeardPendingIntent;
    }

    private static PendingIntent getActionReplyIntent(final Context context) {
        Intent msgReplyIntent = new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(ACTION_MESSAGE_REPLY)
                .putExtra("conversation_id", SCHEDULE_CHANGE_CONVERSATION_ID);

        PendingIntent msgReplyPendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(),
                SCHEDULE_CHANGE_CONVERSATION_ID,
                msgReplyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return msgReplyPendingIntent;
    }


}
