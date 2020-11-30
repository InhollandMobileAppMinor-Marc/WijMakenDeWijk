package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedPost
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post

interface PostsRepository {
    suspend fun getAllPosts(): Set<Post>

    suspend fun getPostById(id: String): Post?

    suspend fun addPost(createdPost: CreatedPost): Post?
}
