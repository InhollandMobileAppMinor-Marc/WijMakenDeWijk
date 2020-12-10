package nl.woonwaard.wij_maken_de_wijk.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.NotificationWorkScheduler
import java.util.concurrent.TimeUnit

class NotificationWorkSchedulerImpl(
    private val context: Context
) : NotificationWorkScheduler {
    override fun schedule() {
        val workRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 15, TimeUnit.MINUTES)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(NotificationWorker.WORKER_ID, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}
