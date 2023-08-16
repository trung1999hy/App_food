package com.example.cookingguide.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.recycleview.BaseRecyclerViewAdapter
import com.example.cookingguide.base.recycleview.BaseViewHolderMore
import com.example.cookingguide.databinding.ItemRcvCategoryBinding
import com.example.cookingguide.model.categories.Categories
import com.example.cookingguide.ui.home.FragmentHomeAdapter.ViewHolder
import com.example.cookingguide.utils.Common

class FragmentHomeAdapter(
    private val context: Context,
    val onClickItem: (Categories) -> Unit
) :
    BaseRecyclerViewAdapter<Categories, ViewHolder>() {

    private var mListData: MutableList<Categories> = ArrayList()

    inner class ViewHolder(private val binding: ItemRcvCategoryBinding) :
        BaseViewHolderMore<Categories>(binding) {
        override fun bindViewHolder(data: Categories) {
            itemView.setOnClickListener {
                onClickItem.invoke(data)
            }
            Glide.with(context).load(data.strCategoryThumb).into(binding.imageItemCategory)
            Common.translateData(data.strCategory) { result ->
                binding.textItemCategory.text = result
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(mList: ArrayList<Categories>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<Categories> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = ItemRcvCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
        setAnimation(context, holder.itemView, R.anim.rcv_fade_in)
    }
}