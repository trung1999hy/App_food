package com.example.cookingguide.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.recycleview.BaseRecyclerViewAdapter
import com.example.cookingguide.base.recycleview.BaseViewHolderMore
import com.example.cookingguide.databinding.ItemRcvFilterBinding
import com.example.cookingguide.model.filter.CategoryDetails
import com.example.cookingguide.utils.Common

class FragmentFilterAdapter(
    private val context: Context,
    val onClickItem: (CategoryDetails) -> Unit
) :
    BaseRecyclerViewAdapter<CategoryDetails, FragmentFilterAdapter.ViewHolder>() {

    private var mListData: MutableList<CategoryDetails> = ArrayList()

    inner class ViewHolder(private val binding: ItemRcvFilterBinding) :
        BaseViewHolderMore<CategoryDetails>(binding) {
        override fun bindViewHolder(data: CategoryDetails) {
            itemView.setOnClickListener {
                onClickItem.invoke(data)
            }
            Glide.with(context).load(data.strMealThumb).into(binding.imageItemFilter)
            Common.translateData(data.strMeal) { result ->
                binding.textItemFilter.text = result
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(mList: ArrayList<CategoryDetails>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<CategoryDetails> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = ItemRcvFilterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
        setAnimation(context, holder.itemView, R.anim.rcv_slide_in_left)
    }
}