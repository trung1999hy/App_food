package com.example.cookingguide.ui.send

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cookingguide.R
import com.example.cookingguide.base.BaseFragmentWithBinding
import com.example.cookingguide.databinding.FragmentSendBinding
import com.example.cookingguide.model.SeenModel
import com.example.cookingguide.ui.inapp.PurchaseInAppActivity
import com.example.cookingguide.ui.lookup.FragmentLookUp
import com.example.cookingguide.utils.Common

class FragmentSend : BaseFragmentWithBinding<FragmentSendBinding>(
    FragmentSendBinding::inflate
) {
    companion object {
        fun newInstance() = FragmentSend()
    }

    private val viewModel: FragmentSendViewModel by viewModels()
    private lateinit var mAdapter: FragmentSendAdapter
    private var mListSend = arrayListOf<SeenModel>()

    override fun initAction() {
        mAdapter = FragmentSendAdapter(requireActivity(), onClickItem = {
            Log.d(TAG, "initAction: ${mListSend[mAdapter.getListItem().indexOf(it)].strMeal}")
            Common.putDataBundle(
                requireActivity(),
                R.id.FragmentLayout,
                FragmentLookUp.newInstance(),
                putBundle(it)
            )
        })
        binding.RcvSend.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.RcvSend.adapter = mAdapter
        binding.coin.setOnClickListener {
            startActivity(Intent( requireActivity(), PurchaseInAppActivity::class.java))
        }
    }

    override fun initData() {
        handleNotData(viewModel.getDataSeen())
        mAdapter.submitList(viewModel.getDataSeen())
        mListSend.addAll(viewModel.getDataSeen())
    }

    private fun handleNotData(listData: ArrayList<SeenModel>) {
        if (listData.size > 0) {
            binding.LllNoti.visibility = View.GONE
            for (mList in listData) {
                Log.d(TAG, "initData: ${mList.strMeal}")
            }
        } else {
            binding.LllNoti.visibility = View.VISIBLE
        }
    }

    private fun putBundle(seenData: SeenModel): Bundle {
        val bundle = Bundle()
        bundle.putString(
            "strMeal", mListSend[mAdapter.getListItem().indexOf(seenData)].strMeal
        )
        bundle.putString(
            "strCategoryThumb",
            mListSend[mAdapter.getListItem().indexOf(seenData)].strMealThumb
        )
        bundle.putString(
            "idCategory", mListSend[mAdapter.getListItem().indexOf(seenData)].idMeal
        )
        bundle.putString(
            "strScreen", "send"
        )
        bundle.putString(
            "strCheck",
            viewModel.getIdMealSqlite(
                mListSend[mAdapter.getListItem().indexOf(seenData)].idMeal
            )
        )
        return bundle
    }
}