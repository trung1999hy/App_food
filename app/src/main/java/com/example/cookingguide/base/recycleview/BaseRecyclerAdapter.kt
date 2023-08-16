package com.example.cookingguide.base.recycleview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

@Retention(AnnotationRetention.SOURCE)
annotation class RecyclerType
abstract class BaseRecyclerAdapter<T : Any, VB : ViewBinding, VH : BaseViewHolder<T, VB>>(
    diffUtilCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffUtilCallback) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<T>?) {
        super.submitList(list ?: emptyList())
    }

    abstract fun getViewHolderDataBinding(parent: ViewGroup, viewType: Int): VB
}
