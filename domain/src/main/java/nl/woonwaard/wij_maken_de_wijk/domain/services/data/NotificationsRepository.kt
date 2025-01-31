package nl.woonwaard.wij_maken_de_wijk.domain.services.data

import nl.woonwaard.wij_maken_de_wijk.domain.models.Notification

interface NotificationsRepository {
    suspend fun getNewNotifications(): Set<Notification>
}
