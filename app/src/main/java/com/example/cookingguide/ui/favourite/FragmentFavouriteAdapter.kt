package com.example.cookingguide.ui.favourite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.recycleview.BaseRecyclerViewAdapter
import com.example.cookingguide.base.recycleview.BaseViewHolderMore
import com.example.cookingguide.databinding.ItemRcvFavouriteBinding
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.utils.Common

class FragmentFavouriteAdapter(
    private val context: Context,
    val onClickItem: (FavouriteModel) -> Unit,
    val onClickOff: (FavouriteModel) -> Unit,
) :
    BaseRecyclerViewAdapter<FavouriteModel, FragmentFavouriteAdapter.ViewHolder>() {

    private var mListData: MutableList<FavouriteModel> = ArrayList()

    inner class ViewHolder(private val binding: ItemRcvFavouriteBinding) :
        BaseViewHolderMore<FavouriteModel>(binding) {
        override fun bindViewHolder(data: FavouriteModel) {
            itemView.setOnClickListener {
                onClickItem.invoke(data)
            }
            binding.imageItemDelete.setOnClickListener {
                onClickOff.invoke(data)
            }
            Glide.with(context).load(data.strMealThumb).into(binding.imageItemFavourite)
            Common.translateData(data.strMeal) { result ->
                binding.textItemFavourite.text = result
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(mList: ArrayList<FavouriteModel>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<FavouriteModel> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = ItemRcvFavouriteBinding.inflate(LayoutInflater.from(context), parent, false)
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