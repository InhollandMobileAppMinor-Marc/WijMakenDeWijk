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
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.domain.models.PostCategory
import nl.woonwaard.wij_maken_de_wijk.domain.models.User
import nl.woonwaard.wij_maken_de_wijk.domain.utils.toSentenceCasing
import nl.woonwaard.wij_maken_de_wijk.ui.R
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.CommentBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PostHeaderBinding
import nl.woonwaard.wij_maken_de_wijk.ui.utils.context
import kotlin.math.max
import kotlin.math.min

class PostDetailsAdapter(
    private val post: LiveData<Post>,
    private val comments: LiveData<Set<Comment>>,
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
                binding.title.visibility = if(showTitleInHeader) View.VISIBLE else View.GONE
                binding.title.text = post.title
                binding.body.text =
                    if(post.deleted) binding.context.getString(R.string.deleted)
                    else post.body
                binding.user.text =
                    if(post.author.deleted) binding.context.getString(R.string.deleted)
                    else post.author.nameWithLocation
                binding.category.setText(when(post.category) {
                    PostCategory.SERVICE -> R.string.service
                    PostCategory.GATHERING -> R.string.gathering
                    PostCategory.SUSTAINABILITY -> R.string.sustainability
                    PostCategory.IDEA -> R.string.idea
                    else -> R.string.unknown
                })
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

                val startFun: (Int, Int) -> Int =
                    if (comment.author.id == currentUser?.id) ::max else ::min
                val endFun: (Int, Int) -> Int =
                    if (comment.author.id == currentUser?.id) ::min else ::max
                val marginStart = binding.view.marginStart
                val marginEnd = binding.view.marginEnd
                binding.view.layoutParams = binding.view.layoutParams.also {
                    if (it is ViewGroup.MarginLayoutParams) {
                        MarginLayoutParamsCompat.setMarginStart(it, startFun(marginStart, marginEnd))
                        MarginLayoutParamsCompat.setMarginEnd(it, endFun(marginStart, marginEnd))
                    }
                }
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
