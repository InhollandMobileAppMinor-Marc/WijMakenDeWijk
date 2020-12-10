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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import nl.woonwaard.wij_maken_de_wijk.domain.models.Notification
import nl.woonwaard.wij_maken_de_wijk.domain.services.NotificationsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import nl.woonwaard.wij_maken_de_wijk.notifications.utils.vectorToBitmap
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

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

    private fun sendNotification(notification: Notification) {
        val intent = Intent(applicationContext, uiClasses.postDetails)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(EXTRA_POST_ID, notification.post)

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_notification_post)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_content)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        notificationManager.notify(
            notification.id.hashCode(),
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_notification_post)
                .setContentTitle(titleNotification)
                .setContentText(subtitleNotification)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(NOTIFICATION_CHANNEL)
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
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val WORKER_ID = "nl.woonwaard.wij_maken_de_wijk.notifications"
        const val NOTIFICATION_CHANNEL = "nl.woonwaard.wij_maken_de_wijk.notifications.replies"
    }
}
