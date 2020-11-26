package nl.woonwaard.wij_maken_de_wijk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository

class PostDetailsViewModel(
    private val commentsRepository: CommentsRepository
) : ViewModel() {
    private val mutablePost = MutableLiveData<Post>()

    val post: LiveData<Post>
        get() = mutablePost

    private val mutableComments = MutableLiveData(emptySet<Comment>())

    val comments: LiveData<Set<Comment>>
        get() = mutableComments

    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    fun loadPostData(post: Post) {
        // Don't load new post data if we're already doing so
        if(isLoading.value == true) return

        mutablePost.postValue(post)
        reloadPostData(post)
    }

    fun reloadPostData(post: Post = this.post.value!!) {
        // Don't load new post data if we're already doing so
        if(isLoading.value == true) return

        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val comments = commentsRepository.getCommentsForPost(post)
            mutableComments.postValue(comments)
            mutableIsLoading.postValue(false)
        }
    }
}
