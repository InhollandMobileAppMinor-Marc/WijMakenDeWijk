package nl.woonwaard.wij_maken_de_wijk.ui.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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

    private var categories: List<String>? = null

    val singleCategory: Boolean
        get() = categories?.size == 1

    private var job: Job? = null

    init {
        reloadPosts()
    }

    fun loadPosts(categories: List<String>? = null) {
        // Don't load new posts if we're already doing so
        if(isLoading.value == true && this.categories == categories) return

        job?.cancel("New job")
        job = null

        if(this.categories != categories) {
            mutablePosts.postValue(emptySet())
            this.categories = categories
        }

        mutableIsLoading.postValue(true)

        job = viewModelScope.launch {
            val posts = postsRepository.getAllPosts(categories)
            mutablePosts.postValue(posts.filter { !it.deleted }.toSet())
            mutableIsLoading.postValue(false)
        }
    }

    fun reloadPosts() = loadPosts(categories)
}
