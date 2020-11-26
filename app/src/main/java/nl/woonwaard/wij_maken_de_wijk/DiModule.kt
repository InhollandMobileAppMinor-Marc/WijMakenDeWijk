package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import nl.woonwaard.wij_maken_de_wijk.fake_api.FakeCommentsRepository
import nl.woonwaard.wij_maken_de_wijk.fake_api.FakePostsRepository
import nl.woonwaard.wij_maken_de_wijk.ui.PinboardOverviewViewModel
import nl.woonwaard.wij_maken_de_wijk.ui.PostDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weMakeTheDistrictModule = module {
    single<PostsRepository> {
        FakePostsRepository()
    }

    single<CommentsRepository> {
        FakeCommentsRepository()
    }

    viewModel {
        PinboardOverviewViewModel(get())
    }

    viewModel {
        PostDetailsViewModel(get())
    }
}
