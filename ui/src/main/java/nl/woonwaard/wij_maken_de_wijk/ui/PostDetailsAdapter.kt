package nl.woonwaard.wij_maken_de_wijk.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import nl.woonwaard.wij_maken_de_wijk.domain.models.Comment
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.ui.PostDetailsActivity.Companion.navigateToPostDetails
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.CommentBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PinboardListItemBinding
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PostHeaderBinding

class PostDetailsAdapter(
    private val post: LiveData<Post>,
    private val comments: LiveData<Set<Comment>>
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
                holder.binding.body.text = post.value!!.body
            }
            is PostDetailsViewHolder.CommentViewHolder -> {
                val comment = comments.value!!.elementAt(position - 1)
                holder.binding.body.text = comment.body
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
