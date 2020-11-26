package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import java.util.*

class FakeCommentsRepository : CommentsRepository {
    override suspend fun getCommentsForPost(post: Post): Set<Comment> {
        return withContext(Dispatchers.IO) {
            delay(1000)
            FakeData.comments.filter {
                it.id in post.commentIds
            }.toSet()
        }
    }

    override suspend fun addCommentToPost(post: Post, comment: Comment) {
        FakeData.comments += comment
        post.commentIds += comment.id
        FakeData.posts.find { it.id == post.id }?.commentIds?.add(comment.id)
    }
}
