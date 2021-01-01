package nl.woonwaard.wij_maken_de_wijk.domain.services.notifications

interface NotificationWorkScheduler {
    fun schedule()

    fun cancel()
}
