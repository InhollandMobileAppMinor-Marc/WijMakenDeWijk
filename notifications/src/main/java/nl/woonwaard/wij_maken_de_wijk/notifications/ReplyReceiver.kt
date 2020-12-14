package nl.woonwaard.wij_maken_de_wijk.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import nl.woonwaard.wij_maken_de_wijk.domain.models.CreatedComment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Notification
import nl.woonwaard.wij_maken_de_wijk.domain.services.CommentsRepository
import nl.woonwaard.wij_maken_de_wijk.domain.services.NotificationManager
import nl.woonwaard.wij_maken_de_wijk.notifications.utils.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ReplyReceiver : BroadcastReceiver(), KoinComponent {
    private val commentsRepository by inject<CommentsRepository>()
    private val notificationManager by inject<NotificationManager>()

    override fun onReceive(context: Context, intent: Intent) {
        val input = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)?.toString()
        val notification = intent.getSerializableExtra(EXTRA_NOTIFICATION) as? Notification

        if (notification != null && !input.isNullOrBlank()) {
            launch {
                val createdComment = commentsRepository.addCommentToPost(
                    notification.post,
                    CreatedComment(input, Date())
                ) ?: return@launch

                val notification = Notification(
                    notification.id,
                    notification.post.copy(comments = notification.post.comments + createdComment.id),
                    notification.comments + createdComment
                )

                notificationManager.cancelNotification(notification)
            }
        }
    }

    companion object {
        const val KEY_TEXT_REPLY = "REPLY"
        const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
    }
}
