package nl.woonwaard.wij_maken_de_wijk.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import nl.woonwaard.wij_maken_de_wijk.domain.models.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.NotificationsRepository
import nl.woonwaard.wij_maken_de_wijk.notifications.utils.putExtra
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.Serializable
import java.util.*

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {
    private val uiClasses by inject<UiClasses>()
    private val api by inject<NotificationsRepository>()

    override suspend fun doWork(): Result {
        val notifications = api.getNewNotifications()
        for (notification in notifications) {
            sendNotification(notification)
        }

        return Result.success()
    }

    @WorkerThread
    private fun sendNotification(notification: Notification) {
        val postDetailsIntent = Intent(applicationContext, uiClasses.postDetails)
            .setAction(Intent.ACTION_VIEW)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            .putExtra(EXTRA_POST, notification.post)
            .putExtra(EXTRA_FROM_NOTIFICATION, true)

        val postDetailsPendingIntent = PendingIntent.getActivity(applicationContext, notification.id.hashCode(), postDetailsIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val postDetailsBubbleIntent = Intent(applicationContext, uiClasses.postDetailsBubble)
            .setAction(Intent.ACTION_VIEW)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            .putExtra(EXTRA_POST, notification.post)
            .putExtra(EXTRA_FROM_NOTIFICATION, true)

        val postDetailsBubblePendingIntent = PendingIntent.getActivity(applicationContext, notification.id.hashCode() + 1, postDetailsBubbleIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val titleNotification = applicationContext.getString(R.string.notification_title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        val selfUser = notification.post.author

        var style = NotificationCompat.MessagingStyle(
            Person.Builder().setName(selfUser.nameWithLocation).build()
        )
            .setGroupConversation(true)
            .setConversationTitle(notification.post.title)

        for(comment in notification.comments) {
            val person = Person.Builder().setName(comment.author.nameWithLocation).build()
            style = style.addMessage(comment.body, comment.timestamp.time, if(selfUser == comment.author) null else person)
        }

        val bubbleData = NotificationCompat.BubbleMetadata.Builder()
            .setIntent(postDetailsBubblePendingIntent)
            .setIcon(IconCompat.createWithResource(applicationContext, R.drawable.ic_notification_post))
            .setDesiredHeight(applicationContext.resources.getDimensionPixelSize(R.dimen.bubble_height))
            .build()

        notificationManager.notify(
            notification.id.hashCode(),
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
                .setBubbleMetadata(bubbleData)
                .setSmallIcon(R.drawable.ic_notification_post)
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
                .build()
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
            applicationContext.getString(R.string.notification_replies_channel),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val EXTRA_POST = "EXTRA_POST"
        const val EXTRA_COMMENTS = "EXTRA_COMMENTS"
        const val EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION"
        const val WORKER_ID = "nl.woonwaard.wij_maken_de_wijk.notifications"
        const val NOTIFICATION_CHANNEL = "nl.woonwaard.wij_maken_de_wijk.notifications.replies"
    }
}
