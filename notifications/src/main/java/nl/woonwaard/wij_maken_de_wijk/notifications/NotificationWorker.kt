package nl.woonwaard.wij_maken_de_wijk.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {
    private val notificationManager by inject<NotificationManager>()

    override suspend fun doWork(): Result {
        notificationManager.checkNotifications()

        return Result.success()
    }

    companion object {
        const val WORKER_ID = "nl.woonwaard.wij_maken_de_wijk.notifications"
    }
}
