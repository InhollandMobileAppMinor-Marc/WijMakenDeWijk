package nl.woonwaard.wij_maken_de_wijk.ui.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

object GenericViewBindingRecyclerViewAdapter {
    class GenericViewBindingViewHolder<VB : ViewBinding>(val binding: VB): RecyclerView.ViewHolder(binding.root)

    inline fun <reified VB : ViewBinding, I> create(
        crossinline size: () -> Int,
        crossinline elementAt: (Int) -> I,
        crossinline inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        crossinline block: (holder: VB, element: I, position: Int) -> Unit
    ): RecyclerView.Adapter<GenericViewBindingViewHolder<VB>> {
        return object : RecyclerView.Adapter<GenericViewBindingViewHolder<VB>>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewBindingViewHolder<VB> {
                val binding = inflater(LayoutInflater.from(parent.context), parent, false)
                return GenericViewBindingViewHolder(binding)
            }

            override fun onBindViewHolder(holder: GenericViewBindingViewHolder<VB>, position: Int) {
                block(holder.binding, elementAt(position), position)
            }

            override fun getItemCount() = size()
        }
    }

    inline fun <reified VB : ViewBinding, I> create(
        iterable: Collection<I>,
        crossinline inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        crossinline block: (holder: VB, element: I, position: Int) -> Unit
    ): RecyclerView.Adapter<GenericViewBindingViewHolder<VB>> {
        return create(iterable::size, iterable::elementAt, inflater, block)
    }

    inline fun <reified VB : ViewBinding, I> create(
        liveIterable: LiveData<Collection<I>>,
        crossinline inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        crossinline block: (holder: VB, element: I, position: Int) -> Unit
    ): RecyclerView.Adapter<GenericViewBindingViewHolder<VB>> {
        return create({ liveIterable.value?.size ?: 0 }, { liveIterable.value!!.elementAt(it) }, inflater, block)
    }
}
