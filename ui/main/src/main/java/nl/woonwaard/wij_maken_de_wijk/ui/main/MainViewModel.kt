package nl.woonwaard.wij_maken_de_wijk.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.AccountManager
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.PostsRepository

class MainViewModel(
    private val accountManager: AccountManager,
    private val postsRepository: PostsRepository
) : ViewModel() {
    private val mutableNotice = MutableLiveData<Post>()

    val notice: LiveData<Post>
        get() = mutableNotice

    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    val isLoggedIn: Boolean
        get() = accountManager.isLoggedIn

    init {
        loadNotice()
    }

    fun loadNotice() {
        // Don't load new posts if we're already doing so
        if(isLoading.value == true) return

        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val posts = postsRepository.getAllPosts(setOf(PostCategory.NOTICE))
            mutableNotice.postValue(posts.filter { !it.deleted }.maxByOrNull { it.timestamp.time })
            mutableIsLoading.postValue(false)
        }
    }
}
