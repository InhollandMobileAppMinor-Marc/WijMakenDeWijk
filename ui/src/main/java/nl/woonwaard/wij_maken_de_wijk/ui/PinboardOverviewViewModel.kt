package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsApi

class PinboardOverviewViewModel(
    private val postsApi: PostsApi
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
        // Don't load new articles if we're already doing so
        if(isLoading.value == true) return

        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val posts = postsApi.getPosts()
            mutablePosts.postValue(posts)
            mutableIsLoading.postValue(false)
        }
    }
}
