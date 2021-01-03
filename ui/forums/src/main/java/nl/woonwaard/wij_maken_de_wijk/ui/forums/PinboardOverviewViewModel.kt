package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CrashReporter
import nl.woonwaard.wij_maken_de_wijk.domain.services.data.PostsRepository

class PinboardOverviewViewModel(
    private val postsRepository: PostsRepository,
    private val crashReporter: CrashReporter
) : ViewModel() {
    private val mutablePosts = MutableLiveData(emptySet<Post>())

    val posts: LiveData<Set<Post>>
        get() = mutablePosts

    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    private val mutableCategories = MutableLiveData<Set<String>>(null)

    val categories: LiveData<Set<String>>
        get() = mutableCategories

    val singleCategory: Boolean
        get() = categories.value?.size == 1

    private var job: Job? = null

    fun changeCategories(categories: Set<String>? = null) {
        if(this.categories.value == categories)
            return

        viewModelScope.launch {
            try {
                job?.cancelAndJoin()
            } catch(error: CancellationException) {
                crashReporter.logSoftError(error)
                error.printStackTrace()
            }
            job = null

            mutablePosts.postValue(emptySet())
            mutableIsLoading.postValue(false)

            mutableCategories.postValue(categories)

            loadPosts(true, categories)
        }
    }

    fun loadPosts() = loadPosts(false, this.categories.value)

    fun loadPosts(categories: Set<String>? = null) = loadPosts(false, categories)

    private fun loadPosts(forced: Boolean, categories: Set<String>? = null) {
        // Don't load new posts if we're already doing so
        if(!forced && isLoading.value == true) return

        mutableIsLoading.postValue(true)

        job = viewModelScope.launch {
            val posts = postsRepository.getAllPosts(categories)
            if(isActive) {
                mutablePosts.postValue(posts.filter { !it.deleted }.toSet())
                mutableIsLoading.postValue(false)
            }
        }
    }

    fun clearPosts() {
        mutablePosts.value = emptySet()
    }
}
