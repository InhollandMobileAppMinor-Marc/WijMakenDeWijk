package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

import android.content.Intent

interface RepairsNavigationService {
    fun getOverviewIntent(): Intent

    fun getCentralHeatingIntent(): Intent

    fun getGlassIntent(): Intent

    fun getLiftIntent(): Intent

    fun getCleaningIntent(): Intent

    fun getGreenMaintenanceIntent(): Intent

    fun getEmergencyIntent(): Intent
}
