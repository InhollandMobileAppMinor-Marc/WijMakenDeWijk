package nl.woonwaard.wij_maken_de_wijk.notifications.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post

fun createShortcut(context: Context, post: Post, comments: Set<Comment>, intent: Intent): ShortcutInfoCompat {
    return ShortcutInfoCompat.Builder(context, post.id)
        .setShortLabel(post.title)
        .setLongLived(true)
        .setCategories(setOf("com.example.android.bubbles.category.TEXT_SHARE_TARGET"))
        .setIntent(intent)
        .setPersons((comments.map { it.author.asPerson } + post.author.asPerson).distinct().toTypedArray())
        .build()
}
