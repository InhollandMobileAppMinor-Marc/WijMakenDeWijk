package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.ApiStatus
import nl.woonwaard.wij_maken_de_wijk.domain.models.Credentials
import nl.woonwaard.wij_maken_de_wijk.domain.models.Registration
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

interface ApiStatusController {
    suspend fun getApiStatus(): ApiStatus
}
