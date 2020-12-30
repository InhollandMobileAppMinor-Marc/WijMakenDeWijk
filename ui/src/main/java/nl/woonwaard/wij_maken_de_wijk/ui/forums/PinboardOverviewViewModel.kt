package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
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

    private val mutableCategories = MutableLiveData<Set<String>>(null)

    val categories: LiveData<Set<String>>
        get() = mutableCategories

    val singleCategory: Boolean
        get() = categories.value?.size == 1

    private var job: Job? = null

    init {
        loadPosts()
    }

    fun changeCategories(categories: Set<String>? = null) {
        if(this.categories.value == categories)
            return

        viewModelScope.launch {
            try {
                job?.cancelAndJoin()
            } catch(error: CancellationException) {
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

    private fun loadPosts(forced: Boolean, categories: Set<String>? = null) {
        // Don't load new posts if we're already doing so
        if(!forced && isLoading.value == true) return

        mutableIsLoading.postValue(true)

        job = viewModelScope.launch {
            Log.i("PINBOARD", "Loading ${categories?.joinToString()}")
            val posts = postsRepository.getAllPosts(categories)
            if(isActive) {
                Log.i("PINBOARD", "Loaded ${categories?.joinToString()}")
                mutablePosts.postValue(posts.filter { !it.deleted }.toSet())
                mutableIsLoading.postValue(false)
            }
        }
    }
}
