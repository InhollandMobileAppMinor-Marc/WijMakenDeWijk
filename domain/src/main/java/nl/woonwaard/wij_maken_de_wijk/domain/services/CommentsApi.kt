package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post

interface CommentsApi {
    suspend fun getCommentsForPost(post: Post): List<Comment>
    suspend fun getCommentsForPost(postId: Int): List<Comment>
}
