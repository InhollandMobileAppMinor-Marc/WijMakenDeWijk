package nl.woonwaard.wij_maken_de_wijk.notifications

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import nl.woonwaard.wij_maken_de_wijk.domain.models.Notification
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.NotificationsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.ForumsNavigationService
import nl.woonwaard.wij_maken_de_wijk.notifications.utils.asPerson
import nl.woonwaard.wij_maken_de_wijk.notifications.utils.createShortcut
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationManagerImpl(private val context: Context) : NotificationManager, KoinComponent {
    private val api by inject<NotificationsRepository>()
    private val forumsNavigationService by inject<ForumsNavigationService>()

    override suspend fun checkNotifications() {
        val notifications = api.getNewNotifications()
        updateShortcuts(notifications)
        for (notification in notifications) {
            sendNotification(notification)
        }
    }

    override fun updateNotification(notification: Notification) {
        updateShortcuts(setOf(notification))
        sendNotification(notification)
    }

    override fun cancelNotification(notification: Notification) {
        NotificationManagerCompat.from(context).cancel(notification.id.hashCode())
    }

    private fun sendNotification(notification: Notification) {
        val postDetailsIntent = forumsNavigationService.getPostDetailsIntent(notification.post, fromNotification = true)
            .setAction(Intent.ACTION_VIEW)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        val postDetailsPendingIntent = PendingIntent.getActivity(context, notification.id.hashCode(), postDetailsIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val postDetailsBubbleIntent = forumsNavigationService.getPostDetailsBubbleIntent(notification.post, fromNotification = true)
            .setAction(Intent.ACTION_VIEW)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        val postDetailsBubblePendingIntent = PendingIntent.getActivity(context, notification.id.hashCode() + 1, postDetailsBubbleIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val replyIntent = Intent(context, ReplyReceiver::class.java)
            .putExtra(ReplyReceiver.EXTRA_NOTIFICATION, notification)

        val replyPendingIntent = PendingIntent.getBroadcast(context, notification.id.hashCode() + 2, replyIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationManager = NotificationManagerCompat.from(context)

        val titleNotification = context.getString(R.string.notification_title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        val selfUser = notification.post.author

        var style = NotificationCompat.MessagingStyle(selfUser.asPerson)
            .setGroupConversation(true)
            .setConversationTitle(notification.post.title)

        for(comment in notification.comments) {
            val person = if(selfUser == comment.author) null else comment.author.asPerson
            style = style.addMessage(comment.body, comment.timestamp.time, person)
        }

        val bubbleData = NotificationCompat.BubbleMetadata.Builder()
            .setIntent(postDetailsBubblePendingIntent)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_woonwaard))
            .setDesiredHeight(context.resources.getDimensionPixelSize(R.dimen.bubble_height))
            .build()

        notificationManager.notify(
            notification.id.hashCode(),
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setBubbleMetadata(bubbleData)
                .setSmallIcon(R.drawable.ic_woonwaard)
                .setContentTitle(titleNotification)
                .setStyle(style)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(postDetailsPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(NOTIFICATION_CHANNEL)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setShowWhen(true)
                .setWhen(notification.comments.last().timestamp.time)
                .setShortcutId(notification.post.id)
                .addAction(
                    NotificationCompat.Action
                        .Builder(
                            IconCompat.createWithResource(context, R.drawable.ic_notification_send),
                            context.getString(R.string.notification_reply_label),
                            replyPendingIntent
                        )
                        .addRemoteInput(
                            RemoteInput.Builder(ReplyReceiver.KEY_TEXT_REPLY)
                                .setLabel(context.getString(R.string.notification_reply_type_hint))
                                .build()
                        )
                        .setAllowGeneratedReplies(true)
                        .build()
                )
                .addAction(
                    NotificationCompat.Action
                        .Builder(
                            IconCompat.createWithResource(context, R.drawable.ic_notification_open),
                            context.getString(R.string.notification_open_label),
                            postDetailsPendingIntent
                        ).build()
                )
                .build()
        )
    }

    private fun updateShortcuts(notifications: Set<Notification>) {
        val shortcuts = notifications.map {
            createShortcut(
                context,
                it.post,
                it.comments,
                forumsNavigationService.getPostDetailsIntent(it.post.id, fromNotification = true)
                    .setAction(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            )
        }

        val maxCount = ShortcutManagerCompat.getMaxShortcutCountPerActivity(context)
        ShortcutManagerCompat.addDynamicShortcuts(
            context,
            if (shortcuts.size > maxCount) shortcuts.take(maxCount) else shortcuts
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManagerCompat) {
        val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL,
            context.getString(R.string.notification_replies_channel),
            android.app.NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "nl.woonwaard.wij_maken_de_wijk.notifications.replies"
    }
}
