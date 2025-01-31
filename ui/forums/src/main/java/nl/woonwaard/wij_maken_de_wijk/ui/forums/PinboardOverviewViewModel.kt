package nl.woonwaard.wij_maken_de_wijk.ui.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
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

    fun reset(done: () -> Unit) {
        mutableIsLoading.value = true
        mutableCategories.value = emptySet()
        mutablePosts.value = emptySet()

        viewModelScope.launch {
            try {
                job?.cancelAndJoin()
            } catch (error: CancellationException) {
                crashReporter.logSoftError(error)
            }
            mutableIsLoading.value = false
            done()
        }
    }

    fun reloadPosts() = loadPosts(this.categories.value)

    fun loadPosts(categories: Set<String>?) {
        // Don't load new posts if we're already doing so
        if(isLoading.value == true) return

        mutableIsLoading.value = true
        mutableCategories.value = categories

        job = viewModelScope.launch {
            val posts = postsRepository.getAllPosts(categories)
            mutablePosts.postValue(posts.filter { !it.deleted && (categories == null || it.category in categories) }.toSet())
            mutableIsLoading.postValue(false)
            job = null
        }
    }

    fun addVote(vote: String, post: Post) = addVote(vote, post.id)

    fun addVote(vote: String, postId: String) {
        mutablePosts.postValue(posts.value?.map {
            if(it.id == postId) it.copy(vote = vote)
            else it
        }?.toSet() ?: emptySet())

        viewModelScope.launch {
            postsRepository.addVote(vote, postId)
        }
    }

    fun removeVote(post: Post) = removeVote(post.id)

    fun removeVote(postId: String) {
        mutablePosts.postValue(posts.value?.map {
            if(it.id == postId) it.copy(vote = null)
            else it
        }?.toSet() ?: emptySet())

        viewModelScope.launch {
            postsRepository.addVote("", postId)
        }
    }
}
