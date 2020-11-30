package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.api.WmdwApi
import nl.woonwaard.wij_maken_de_wijk.domain.services.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.UsersRepository
import nl.woonwaard.wij_maken_de_wijk.ui.LoginViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.PinboardOverviewViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.PostDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weMakeTheDistrictModule = module {
    single {
        WmdwApi()
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

    viewModel {
        PinboardOverviewViewModel(get())
    }

    viewModel {
        PostDetailsViewModel(get())
    }

    viewModel {
        LoginViewModel(get())
    }
}
