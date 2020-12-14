package nl.woonwaard.wij_maken_de_wijk.ui.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedPost
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import java.util.*

class CreatePostViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {
    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    private val mutableCreatedPost = MutableLiveData<Post>()

    val createdPost: LiveData<Post>
        get() = mutableCreatedPost

    fun createPost(title: String, type: String, message: String) {
        mutableIsLoading.postValue(true)

        viewModelScope.launch {
            val result = postsRepository.addPost(CreatedPost(title, type, message, Date()))
            mutableIsLoading.postValue(false)
            mutableCreatedPost.postValue(result)
        }
    }
}
