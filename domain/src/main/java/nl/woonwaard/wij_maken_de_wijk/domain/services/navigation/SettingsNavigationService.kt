package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

import android.content.Intent

interface SettingsNavigationService {
    fun getOverviewIntent(): Intent

    fun getChangeEmailIntent(): Intent

    fun getChangePasswordIntent(): Intent

    fun getGenerateCodeIntent(): Intent
}
