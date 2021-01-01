package nl.woonwaard.wij_maken_de_wijk.domain.services.notifications

import nl.woonwaard.wij_maken_de_wijk.domain.models.Notification

interface NotificationManager {
    suspend fun checkNotifications()

    fun updateNotification(notification: Notification)

    fun cancelNotification(notification: Notification)
}
