package nl.woonwaard.wij_maken_de_wijk.ui.forums

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.domain.models.User
import nl.woonwaard.wij_maken_de_wijk.ui.core.context
import nl.woonwaard.wij_maken_de_wijk.ui.forums.databinding.CommentBinding
import nl.woonwaard.wij_maken_de_wijk.ui.forums.databinding.PostHeaderBinding
import kotlin.math.max
import kotlin.math.min

class PostDetailsAdapter(
    private val post: LiveData<Post>,
    private val comments: LiveData<Set<Comment>>,
    private val deleteComment: ((comment: Comment) -> Unit)? = null,
    private val reportComment: ((comment: Comment) -> Unit)? = null,
    private val currentUser: User? = null,
    private val showTitleInHeader: Boolean = false
) : RecyclerView.Adapter<PostDetailsAdapter.PostDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailsViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val binding = PostHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PostDetailsViewHolder.HeaderViewHolder(binding)
            }
            TYPE_COMMENT -> {
                val binding = CommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PostDetailsViewHolder.CommentViewHolder(binding)
            }
            else -> throw IllegalStateException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: PostDetailsViewHolder, position: Int) {
        when (holder) {
            is PostDetailsViewHolder.HeaderViewHolder -> {
                val binding = holder.binding
                val post = post.value!!
                binding.title.visibility = if (showTitleInHeader) View.VISIBLE else View.GONE
                binding.title.text = post.title
                binding.body.text =
                    if (post.deleted) binding.context.getString(R.string.deleted)
                    else post.body
                binding.user.text =
                    if (post.author.deleted) binding.context.getString(R.string.deleted)
                    else post.author.nameWithLocation
                binding.category.setText(
                    when (post.category) {
                        PostCategory.SERVICE -> R.string.service
                        PostCategory.GATHERING -> R.string.gathering
                        PostCategory.SUSTAINABILITY -> R.string.sustainability
                        PostCategory.IDEA -> R.string.idea
                        PostCategory.OTHER -> R.string.other
                        else -> R.string.unknown
                    }
                )
                binding.time.text = DateUtils.getRelativeTimeSpanString(
                    post.timestamp.time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                )
            }
            is PostDetailsViewHolder.CommentViewHolder -> {
                val binding = holder.binding
                val comment = comments.value!!.elementAt(position - 1)

                binding.body.text =
                    if (comment.deleted) binding.context.getString(R.string.deleted)
                    else comment.body
                binding.user.text =
                    if (comment.author.deleted) binding.context.getString(R.string.deleted)
                    else comment.author.nameWithLocation
                binding.time.text = DateUtils.getRelativeTimeSpanString(
                    comment.timestamp.time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                )

                val canDeleteComment =
                    deleteComment != null && (comment.author == currentUser || currentUser?.isAdmin ?: false)

                if (canDeleteComment || reportComment != null) {
                    binding.root.isFocusable = true
                    binding.root.isClickable = true
                    val items = mutableSetOf<Pair<Int, ((comment: Comment) -> Unit)?>>()
                    if (canDeleteComment)
                        items += R.string.delete to deleteComment
                    if (reportComment != null)
                        items += R.string.report to reportComment
                    binding.root.setOnClickListener {
                        MaterialAlertDialogBuilder(binding.context)
                            .setTitle(comment.body.replace("\n", "  "))
                            .setItems(
                                items.map {
                                    binding.context.getString(it.first)
                                }.toTypedArray()
                            ) { dialog, which ->
                                items.elementAtOrNull(which)?.second?.invoke(comment)
                                dialog.dismiss()
                            }
                            .show()
                    }
                } else {
                    binding.root.setOnClickListener(null)
                    binding.root.isClickable = false
                    binding.root.isFocusable = false
                }

                val startFun: (Int, Int) -> Int =
                    if (comment.author.id == currentUser?.id) ::max else ::min
                val endFun: (Int, Int) -> Int =
                    if (comment.author.id == currentUser?.id) ::min else ::max
                val marginStart = binding.view.marginStart
                val marginEnd = binding.view.marginEnd
                binding.view.layoutParams = binding.view.layoutParams.also {
                    if (it is ViewGroup.MarginLayoutParams) {
                        MarginLayoutParamsCompat.setMarginStart(
                            it,
                            startFun(marginStart, marginEnd)
                        )
                        MarginLayoutParamsCompat.setMarginEnd(it, endFun(marginStart, marginEnd))
                    }
                }

                val backgrounds = setOf(
                    R.drawable.comment_background,
                    R.drawable.comment_background_brown,
                    R.drawable.comment_background_green,
                    R.drawable.comment_background_pink,
                    R.drawable.comment_background_purple,
                    R.drawable.comment_background_gold,
                )

                binding.view.setBackgroundResource(
                    if(comment.author.isAdmin) R.drawable.comment_background_woonwaard
                    else backgrounds.random()
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) TYPE_HEADER else TYPE_COMMENT
    }

    override fun getItemCount() = (comments.value?.size ?: 0) + if(post.value == null) 0 else 1

    sealed class PostDetailsViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        class HeaderViewHolder(val binding: PostHeaderBinding) : PostDetailsViewHolder(binding.root)
        class CommentViewHolder(val binding: CommentBinding) : PostDetailsViewHolder(binding.root)
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_COMMENT = 1
    }
}
