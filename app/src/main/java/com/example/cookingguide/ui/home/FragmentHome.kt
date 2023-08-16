package com.example.cookingguide.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.BaseFragmentWithBinding
import com.example.cookingguide.databinding.FragmentHomeBinding
import com.example.cookingguide.model.categories.Categories
import com.example.cookingguide.ui.MainApp
import com.example.cookingguide.ui.filter.FragmentFilter
import com.example.cookingguide.ui.inapp.PurchaseInAppActivity
import com.example.cookingguide.utils.Common

class FragmentHome : BaseFragmentWithBinding<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    companion object {
        fun newInstance() = FragmentHome()
    }

    private var mAdapter: FragmentHomeAdapter? = null
    private var mList = arrayListOf<Categories>()
    private val viewModel: FragmentHomeViewModel by viewModels()

    @SuppressLint("CheckResult")
    override fun initAction() {
        binding.ProgressLoadHome.visibility = View.VISIBLE
        Glide.with(requireActivity()).load(resources.getString(R.string.image_title))
            .into(binding.ImageTitle)
        mAdapter = FragmentHomeAdapter(requireActivity(), onClickItem = {
            Common.putDataBundle(
                requireActivity(),
                R.id.FragmentLayout,
                FragmentFilter.newInstance(),
                putBundle(it)
            )
        })
        binding.RcvCategories.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.RcvCategories.adapter = mAdapter
        binding.coin.setOnClickListener {
            startActivity(Intent( requireActivity(), PurchaseInAppActivity::class.java))
        }
    }

    override fun initData() {
        viewModel.categories.observe(requireActivity(), Observer {
            mAdapter?.submitList(it)
            mList = it
            binding.ProgressLoadHome.visibility = View.GONE
        })
        getCoin()
    }
    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }


    private fun putBundle(categories: Categories): Bundle {
        val bundle = Bundle()
        bundle.putString(
            "strCategory", mList[mAdapter?.getListItem()?.indexOf(categories)!!].strCategory
        )
        bundle.putString(
            "strCategoryThumb",
            mList[mAdapter?.getListItem()?.indexOf(categories)!!].strCategoryThumb
        )
        return bundle
    }
}