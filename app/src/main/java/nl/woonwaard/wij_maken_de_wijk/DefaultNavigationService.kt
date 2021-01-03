package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.*

class DefaultNavigationService(
    override val authentication: AuthenticationNavigationService,
    override val forums: ForumsNavigationService,
    override val main: MainNavigationService,
    override val repairs: RepairsNavigationService,
    override val settings: SettingsNavigationService,
    override val neighborhoodMediation: NeighborhoodMediationNavigationService
) : NavigationService
