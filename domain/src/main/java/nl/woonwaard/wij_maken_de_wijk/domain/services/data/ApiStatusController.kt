package nl.woonwaard.wij_maken_de_wijk.domain.services.data

import nl.woonwaard.wij_maken_de_wijk.domain.models.ApiStatus

interface ApiStatusController {
    suspend fun getApiStatus(): ApiStatus
}
