package nl.woonwaard.wij_maken_de_wijk.fake_api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostType
import nl.woonwaard.wij_maken_de_wijk.domain.services.PostsRepository
import java.util.*

class FakePostsRepository : PostsRepository {
    override suspend fun getPosts(types: List<PostType>): Set<Post> {
        return withContext(Dispatchers.IO) {
            delay(1000)
            FakeData.posts.filter {
                it.type in types
            }.toSet()
        }
    }

    override suspend fun getPostById(id: Int) = getPosts().firstOrNull { it.id == id }

    override suspend fun addPost(post: Post) {
        FakeData.posts += post
    }
}
