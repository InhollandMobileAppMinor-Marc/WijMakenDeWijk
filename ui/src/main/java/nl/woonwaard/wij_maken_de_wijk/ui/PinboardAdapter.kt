package nl.woonwaard.wij_maken_de_wijk.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import nl.woonwaard.wij_maken_de_wijk.domain.models.Post
import nl.woonwaard.wij_maken_de_wijk.ui.databinding.PinboardListItemBinding

class PinboardAdapter(private val posts: LiveData<Set<Post>>) : RecyclerView.Adapter<PinboardAdapter.PinboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinboardViewHolder {
        val binding = PinboardListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PinboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PinboardViewHolder, position: Int) {
        val article = posts.value!!.elementAt(position)

        holder.binding.title.text = article.title

        holder.binding.content.text = article.body.replace('\n', ' ')
    }

    override fun getItemCount() = posts.value?.size ?: 0

    class PinboardViewHolder(val binding: PinboardListItemBinding): RecyclerView.ViewHolder(binding.root)
}
