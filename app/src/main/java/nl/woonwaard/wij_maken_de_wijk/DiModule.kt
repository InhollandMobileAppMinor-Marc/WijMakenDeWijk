package nl.woonwaard.wij_maken_de_wijk

import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsApi
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsApi
import nl.woonwaard.wij_maken_de_wijk.fake_api.FakeCommentsApi
import nl.woonwaard.wij_maken_de_wijk.fake_api.FakePostsApi
import nl.woonwaard.wij_maken_de_wijk.ui.PinboardOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weMakeTheDistrictModule = module {
    single<PostsApi> {
        FakePostsApi()
    }

    single<CommentsApi> {
        FakeCommentsApi()
    }

    viewModel {
        PinboardOverviewViewModel(get())
    }
}
