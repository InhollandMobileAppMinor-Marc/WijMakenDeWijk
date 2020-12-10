package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.api.WmdwApi
import nl.woonwaard.wij_maken_de_wijk.domain.services.*
import nl.woonwaard.wij_maken_de_wijk.notifications.NotificationWorkSchedulerImpl
import nl.woonwaard.wij_maken_de_wijk.notifications.NotificationWorker
import nl.woonwaard.wij_maken_de_wijk.notifications.UiClasses
import nl.woonwaard.wij_maken_de_wijk.ui.SplashScreenViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.LoginViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.authentication.RegistrationViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.CreatePostViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PinboardOverviewViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsActivity
import nl.woonwaard.wij_maken_de_wijk.ui.forums.PostDetailsViewModel
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

    single<UiClasses> {
        object : UiClasses {
            override val postDetails: Class<*>
                get() = PostDetailsActivity::class.java
        }
    }

    single<NotificationWorkScheduler> {
        NotificationWorkSchedulerImpl(get())
    }

    viewModel {
        SplashScreenViewModel(get(), get())
    }

    viewModel {
        PinboardOverviewViewModel(get())
    }

    viewModel {
        PostDetailsViewModel(get(), get())
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
        SettingsViewModel(get())
    }
}
