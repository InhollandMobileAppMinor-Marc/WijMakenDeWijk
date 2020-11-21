package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsApi

class FakeCommentsApi : CommentsApi {
    override suspend fun getCommentsForPost(post: Post) = getCommentsForPost(post.id)

    override suspend fun getCommentsForPost(postId: Int): Set<Comment> {
        return withContext(Dispatchers.IO) {
            delay(500)
            emptySet()
        }
    }
}
