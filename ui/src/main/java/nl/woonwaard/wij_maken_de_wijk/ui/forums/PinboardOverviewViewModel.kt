package nl.woonwaard.wij_maken_de_wijk.ui.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository

class PinboardOverviewViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {
    private val mutablePosts = MutableLiveData(emptySet<Post>())

    val posts: LiveData<Set<Post>>
        get() = mutablePosts

    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    init {
        loadPosts()
    }

    fun loadPosts() {
        // Don't load new posts if we're already doing so
        if(isLoading.value == true) return

        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val posts = postsRepository.getAllPosts()
            mutablePosts.postValue(posts.filter { !it.deleted }.toSet())
            mutableIsLoading.postValue(false)
        }
    }
}
