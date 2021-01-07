package nl.woonwaard.wij_maken_de_wijk.domain.services.data

import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedPost
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post

interface PostsRepository {
    suspend fun getAllPosts(categories: Set<String>? = null): Set<Post>

    suspend fun getPostById(id: String): Post?

    suspend fun addPost(createdPost: CreatedPost): Post?

    suspend fun reportPost(post: Post): Boolean

    suspend fun reportPost(id: String): Boolean

    suspend fun deletePost(post: Post): Boolean

    suspend fun deletePost(id: String): Boolean
}
