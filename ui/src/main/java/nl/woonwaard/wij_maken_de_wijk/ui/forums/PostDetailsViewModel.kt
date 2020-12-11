package nl.woonwaard.wij_maken_de_wijk.ui.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedComment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import java.util.*

class PostDetailsViewModel(
    private val postsRepository: PostsRepository,
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

    var isFromNotification = false

    fun loadPostData(postId: String) {
        // Don't load new post data if we're already doing so
        if(isLoading.value == true) return

        viewModelScope.launch {
            val post = postsRepository.getPostById(postId) ?: return@launch
            loadPostData(post)
        }
    }

    fun loadPostData(post: Post) {
        // Don't load new post data if we're already doing so
        if(isLoading.value == true) return

        mutablePost.postValue(post)
        reloadPostData(post)
    }

    fun loadPostData(post: Post, comments: Set<Comment>) {
        // Don't load new post data if we're already doing so
        if(isLoading.value == true) return

        mutablePost.postValue(post)
        mutableComments.postValue(comments)
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

    fun addComment(text: String) {
        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val comment = commentsRepository.addCommentToPost(post.value!!, CreatedComment(text, Date()))
            if(comment != null)
                mutableComments.postValue(comments.value!! + comment)
            mutableIsLoading.postValue(false)
        }
    }
}
