package com.example.cookingguide.ui.send

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.recycleview.BaseRecyclerViewAdapter
import com.example.cookingguide.base.recycleview.BaseViewHolderMore
import com.example.cookingguide.databinding.ItemRcvSeenBinding
import com.example.cookingguide.model.SeenModel
import com.example.cookingguide.utils.Common

class FragmentSendAdapter(
    private val context: Context,
    val onClickItem: (SeenModel) -> Unit
) :
    BaseRecyclerViewAdapter<SeenModel, FragmentSendAdapter.ViewHolder>() {

    private var mListData: MutableList<SeenModel> = ArrayList()

    inner class ViewHolder(private val binding: ItemRcvSeenBinding) :
        BaseViewHolderMore<SeenModel>(binding) {
        override fun bindViewHolder(data: SeenModel) {
            itemView.setOnClickListener {
                onClickItem.invoke(data)
            }
            Glide.with(context).load(data.strMealThumb).into(binding.imageItemSeen)
            Common.translateData(data.strMeal) { result ->
                binding.textItemSeen.text = result
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(mList: ArrayList<SeenModel>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<SeenModel> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = ItemRcvSeenBinding.inflate(LayoutInflater.from(context), parent, false)
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