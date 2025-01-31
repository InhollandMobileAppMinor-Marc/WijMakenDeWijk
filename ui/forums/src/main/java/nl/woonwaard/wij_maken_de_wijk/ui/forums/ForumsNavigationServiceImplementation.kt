package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.content.Context
import android.content.Intent
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.User
import nl.woonwaard.wij_maken_de_wijk.domain.services.navigation.ForumsNavigationService
import nl.woonwaard.wij_maken_de_wijk.domain.utils.putSerializableArrayExtra

class ForumsNavigationServiceImplementation(
        val context: Context
) : ForumsNavigationService {

    override fun getOverviewIntent(categories: Set<String>?): Intent {
        val intent = Intent(context, PinboardOverviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if(categories != null)
            intent.putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
        return intent
    }

    override fun getCreatePostIntent(categories: Set<String>?): Intent {
        val intent = Intent(context, CreatePostActivity::class.java)
        if(categories != null)
            intent.putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
        return intent
    }

    override fun getPostDetailsIntent(
        post: Post,
        comments: Set<Comment>?,
        fromNotification: Boolean,
        currentUser: User?
    ): Intent {
        val intent = Intent(context, PostDetailsActivity::class.java)
        intent.putExtra(EXTRA_POST, post)
        intent.putExtra(EXTRA_FROM_NOTIFICATION, fromNotification)
        if (comments != null)
            intent.putSerializableArrayExtra(EXTRA_COMMENTS, comments.toTypedArray())
        if(currentUser != null)
            intent.putExtra(EXTRA_CURRENT_USER, currentUser)
        return intent
    }

    override fun getPostDetailsIntent(
        post: String,
        fromNotification: Boolean,
        currentUser: User?
    ): Intent {
        val intent = Intent(context, PostDetailsActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, post)
        intent.putExtra(EXTRA_FROM_NOTIFICATION, fromNotification)
        if(currentUser != null)
            intent.putExtra(EXTRA_CURRENT_USER, currentUser)
        return intent
    }

    override fun getPostDetailsBubbleIntent(
        post: Post,
        comments: Set<Comment>?,
        fromNotification: Boolean,
        currentUser: User?
    ): Intent {
        val intent = Intent(context, PostDetailsBubbleActivity::class.java)
        intent.putExtra(EXTRA_POST, post)
        intent.putExtra(EXTRA_FROM_NOTIFICATION, fromNotification)
        if (comments != null)
            intent.putSerializableArrayExtra(EXTRA_COMMENTS, comments.toTypedArray())
        if(currentUser != null)
            intent.putExtra(EXTRA_CURRENT_USER, currentUser)
        return intent
    }

    override fun getPostDetailsBubbleIntent(
        post: String,
        fromNotification: Boolean,
        currentUser: User?
    ): Intent {
        val intent = Intent(context, PostDetailsBubbleActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, post)
        intent.putExtra(EXTRA_FROM_NOTIFICATION, fromNotification)
        if(currentUser != null)
            intent.putExtra(EXTRA_CURRENT_USER, currentUser)
        return intent
    }

    companion object {
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
        const val EXTRA_POST = "EXTRA_POST"
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
        const val EXTRA_COMMENTS = "EXTRA_COMMENTS"
        const val EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION"
        const val EXTRA_CURRENT_USER = "EXTRA_CURRENT_USER"
    }
}
