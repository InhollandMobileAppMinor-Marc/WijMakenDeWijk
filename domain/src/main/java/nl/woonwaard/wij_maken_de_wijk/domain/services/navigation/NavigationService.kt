package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

interface NavigationService {
    val authentication: AuthenticationNavigationService

    val forums: ForumsNavigationService

    val main: MainNavigationService

    val repairs: RepairsNavigationService

    val settings: SettingsNavigationService

    val neighborhoodMediation: NeighborhoodMediationNavigationService
}
