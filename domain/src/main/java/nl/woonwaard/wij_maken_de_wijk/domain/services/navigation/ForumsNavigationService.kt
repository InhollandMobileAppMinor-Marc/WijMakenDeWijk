package nl.woonwaard.wij_maken_de_wijk.domain.services.navigation

import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.User

interface ForumsNavigationService {
    fun getOverviewIntent(categories: Set<String>? = null): Intent

    fun getCreatePostIntent(categories: Set<String>? = null): Intent

    fun getPostDetailsIntent(
        post: Post,
        comments: Set<Comment>? = null,
        fromNotification: Boolean = false,
        currentUser: User? = null
    ): Intent

    fun getPostDetailsIntent(
        post: String,
        fromNotification: Boolean = false,
        currentUser: User? = null
    ): Intent

    fun getPostDetailsBubbleIntent(
        post: Post,
        comments: Set<Comment>? = null,
        fromNotification: Boolean = true,
        currentUser: User? = null
    ): Intent

    fun getPostDetailsBubbleIntent(
        post: String,
        fromNotification: Boolean = true,
        currentUser: User? = null
    ): Intent
}
