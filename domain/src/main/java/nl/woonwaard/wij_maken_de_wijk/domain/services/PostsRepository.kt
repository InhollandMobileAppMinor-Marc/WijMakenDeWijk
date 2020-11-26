package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostType

interface PostsRepository {
    suspend fun getPosts(
        types: List<PostType> = listOf(PostType.SERVICE, PostType.SUSTAINABILITY, PostType.EVENT)
    ): Set<Post>

    suspend fun getPostById(id: Int): Post?

    suspend fun addPost(post: Post)
}
