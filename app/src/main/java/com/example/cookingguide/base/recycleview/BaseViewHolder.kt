package com.example.cookingguide.base.recycleview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<T : Any, VB : ViewBinding>(
    private val binding: VB, val itemClickListener: ((T) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private var _itemData: T? = null
    val itemData get() = _itemData

    init {
        itemView.setOnClickListener {
            itemData?.let { it1 -> itemClickListener?.invoke(it1) }
        }
    }

    open fun bind(itemData: T) {
        this._itemData = itemData
    }

}
