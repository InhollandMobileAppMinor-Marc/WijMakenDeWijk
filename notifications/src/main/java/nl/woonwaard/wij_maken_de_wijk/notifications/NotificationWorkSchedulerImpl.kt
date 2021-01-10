package nl.woonwaard.wij_maken_de_wijk.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationWorkScheduler
import java.util.concurrent.TimeUnit

class NotificationWorkSchedulerImpl(
    private val context: Context
) : NotificationWorkScheduler {
    private val workManager by lazy {
        WorkManager.getInstance(context)
    }

    override fun schedule() {
        val workRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 15, TimeUnit.MINUTES)
            .setInitialDelay(15, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(NotificationWorker.WORKER_ID, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }

    override fun cancel() {
        workManager.cancelAllWork()
    }
}
