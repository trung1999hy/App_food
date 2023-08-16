package com.example.cookingguide.ui.filter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.BaseFragmentWithBinding
import com.example.cookingguide.databinding.FragmentFilterBinding
import com.example.cookingguide.local.InstructionsHelper
import com.example.cookingguide.model.filter.CategoryDetails
import com.example.cookingguide.ui.MainApp
import com.example.cookingguide.ui.inapp.PurchaseInAppActivity
import com.example.cookingguide.ui.lookup.FragmentLookUp
import com.example.cookingguide.utils.Common

class FragmentFilter : BaseFragmentWithBinding<FragmentFilterBinding>(
    FragmentFilterBinding::inflate
) {
    companion object {
        fun newInstance() = FragmentFilter()

        fun dataInstance(data: String): FragmentFilter {
            val fragment = FragmentFilter()
            val args = Bundle()
            args.putString("strCategory", data)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: FragmentFilterViewModel by viewModels()
    private var mAdapter: FragmentFilterAdapter? = null
    private var mList = arrayListOf<CategoryDetails>()

    override fun initAction() {
        binding.ProgressLoadFilter.visibility = View.VISIBLE
        Common.translateData(arguments?.getString("strCategory").toString()) {result ->
            binding.TextTitleFilter.text = result
        }
        Glide.with(requireActivity()).load(arguments?.getString("strCategoryThumb"))
            .into(binding.ImageTitleFilter)

        mAdapter = FragmentFilterAdapter(requireActivity(), onClickItem = {
            Common.putDataBundle(
                requireActivity(),
                R.id.FragmentLayout,
                FragmentLookUp.newInstance(),
                putBundle(it)
            )

            viewModel.inputDataSqlite(
                setDataSqlite(it)
            )
        })
        binding.RcvFilter.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.RcvFilter.adapter = mAdapter
        binding.coin.setOnClickListener {
            startActivity(Intent( requireActivity(), PurchaseInAppActivity::class.java))
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun initData() {
        viewModel.filter.observe(viewLifecycleOwner, Observer {
            mAdapter?.submitList(it)
            mList = it
            binding.ProgressLoadFilter.visibility = View.GONE
        })
        viewModel.fetchCategoryDetails(arguments?.getString("strCategory").toString())
        getCoin()
    }

    private fun putBundle(categoryDetails: CategoryDetails): Bundle {
        val bundle = Bundle()
        bundle.putString(
            "strMeal", mList[mAdapter?.getListItem()?.indexOf(categoryDetails)!!].strMeal
        )
        bundle.putString(
            "strCategoryThumb",
            mList[mAdapter?.getListItem()?.indexOf(categoryDetails)!!].strMealThumb
        )
        bundle.putString(
            "idCategory", mList[mAdapter?.getListItem()!!.indexOf(categoryDetails)].idMeal
        )
        bundle.putString(
            "strThumb", arguments?.getString("strCategoryThumb").toString()
        )
        bundle.putString(
            "strCheck",
            viewModel.getIdMealSqlite(
                mList[mAdapter?.getListItem()!!.indexOf(categoryDetails)].idMeal
            )
        )
        return bundle
    }
    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }

    private fun setDataSqlite(categoryDetails: CategoryDetails): CategoryDetails {
        return CategoryDetails(
            idMeal = mList[mAdapter?.getListItem()!!.indexOf(categoryDetails)].idMeal,
            strMeal = mList[mAdapter?.getListItem()?.indexOf(categoryDetails)!!].strMeal,
            strMealThumb = mList[mAdapter?.getListItem()?.indexOf(categoryDetails)!!].strMealThumb,
            strCategoryThumb = arguments?.getString("strCategoryThumb").toString()
        )
    }
}