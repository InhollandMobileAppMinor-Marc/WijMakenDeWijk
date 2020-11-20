package nl.woonwaard.wij_maken_de_wijk.fake_api

import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsApi

class FakeCommentsApi : CommentsApi {
    override suspend fun getCommentsForPost(post: Post) = getCommentsForPost(post.id)

    override suspend fun getCommentsForPost(postId: Int): List<Comment> {
        return emptyList()
    }
}
