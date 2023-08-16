package com.example.cookingguide.base.recycleview

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingguide.R

abstract class BaseRecyclerViewAdapter<T : Any, VH : BaseViewHolderMore<T>>() :
    RecyclerView.Adapter<VH>() {

    interface OnClickItem {
        fun isClickItem(view: View, position: Int, isCheck: Boolean)
    }

    abstract fun submitList(mList: ArrayList<T>)
    abstract fun getListItem(): MutableList<T>

    open fun setAnimation(context: Context,viewToAnimate: View, anim: Int) {
        viewToAnimate.startAnimation(AnimationUtils.loadAnimation(context, anim))
    }
}