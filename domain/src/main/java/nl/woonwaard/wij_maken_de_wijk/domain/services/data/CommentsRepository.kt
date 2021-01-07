package nl.woonwaard.wij_maken_de_wijk.domain.services.data

import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedComment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post

interface CommentsRepository {
    suspend fun getCommentsForPost(post: Post): Set<Comment>

    suspend fun addCommentToPost(post: Post, comment: CreatedComment): Comment?

    suspend fun reportComment(comment: Comment): Boolean

    suspend fun reportComment(id: String): Boolean

    suspend fun deleteComment(comment: Comment): Boolean

    suspend fun deleteComment(id: String): Boolean
}
