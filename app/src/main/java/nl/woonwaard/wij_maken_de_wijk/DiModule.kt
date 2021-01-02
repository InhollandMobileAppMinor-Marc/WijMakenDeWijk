package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.api.WmdwApi
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.*
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.notifications.NotificationWorkScheduler
import nl.woonwaard.wij_maken_de_wijk.notifications.NotificationManagerImpl
import nl.woonwaard.wij_maken_de_wijk.notifications.NotificationWorkSchedulerImpl
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.AuthenticationNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.LoginViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.RegistrationViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.CreatePostViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.ForumsNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PinboardOverviewViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.main.MainNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.main.MainViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.main.SplashScreenViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.repairs.RepairsNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.settings.SettingsNavigationServiceImplementation
import nl.woonwaard.wij_maken_de_wijk.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weMakeTheDistrictModule = module {
    single {
        WmdwApi(get())
    }

    single<AccountManager> {
        get<WmdwApi>()
    }

    single<UsersRepository> {
        get<WmdwApi>()
    }

    single<PostsRepository> {
        get<WmdwApi>()
    }

    single<CommentsRepository> {
        get<WmdwApi>()
    }

    single<NotificationsRepository> {
        get<WmdwApi>()
    }

    single<ApiStatusController> {
        get<WmdwApi>()
    }

    single<NotificationManager> {
        NotificationManagerImpl(get())
    }

    single<NotificationWorkScheduler> {
        NotificationWorkSchedulerImpl(get())
    }

    single<AuthenticationNavigationService> {
        AuthenticationNavigationServiceImplementation(get())
    }

    single<ForumsNavigationService> {
        ForumsNavigationServiceImplementation(get())
    }

    single<MainNavigationService> {
        MainNavigationServiceImplementation(get())
    }

    single<RepairsNavigationService> {
        RepairsNavigationServiceImplementation(get())
    }

    single<SettingsNavigationService> {
        SettingsNavigationServiceImplementation(get())
    }

    single<NavigationService> {
        DefaultNavigationService(get(), get(), get(), get(), get())
    }

    viewModel {
        MainViewModel(get(), get())
    }

    viewModel {
        SplashScreenViewModel(get(), get(), get())
    }

    viewModel {
        PinboardOverviewViewModel(get())
    }

    viewModel {
        PostDetailsViewModel(get(), get(), get())
    }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        CreatePostViewModel(get())
    }

    viewModel {
        RegistrationViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}
