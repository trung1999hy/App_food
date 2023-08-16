package com.example.cookingguide.ui.favourite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.BaseFragmentWithBinding
import com.example.cookingguide.databinding.FragmentFavouriteBinding
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.ui.MainApp
import com.example.cookingguide.ui.inapp.PurchaseInAppActivity
import com.example.cookingguide.ui.lookup.FragmentLookUp
import com.example.cookingguide.utils.Common
import com.google.android.material.snackbar.Snackbar

class FragmentFavourite : BaseFragmentWithBinding<FragmentFavouriteBinding>(
    FragmentFavouriteBinding::inflate
) {
    companion object {
        fun newInstance() = FragmentFavourite()
    }

    private val viewModel: FragmentFavouriteViewModel by viewModels()
    private lateinit var mAdapter: FragmentFavouriteAdapter
    private var mListSend = arrayListOf<FavouriteModel>()

    override fun initAction() {
        mAdapter = FragmentFavouriteAdapter(requireActivity(),
            onClickItem = {
                Common.putDataBundle(
                    requireActivity(),
                    R.id.FragmentLayout,
                    FragmentLookUp.newInstance(),
                    putBundle(it)
                )
            }, onClickOff = {
                createSnackBar(binding.root, it).show()
            })

        binding.RcvFavourite.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.RcvFavourite.adapter = mAdapter
        binding.coin.setOnClickListener {
            startActivity(Intent( requireActivity(),PurchaseInAppActivity::class.java))
        }
    }

    override fun initData() {
        viewModel.getDataFavourite().clear()
        mListSend.clear()
        handleNotData(viewModel.getDataFavourite())
        mAdapter.submitList(mListSend)
        mListSend.addAll(viewModel.getDataFavourite())
        getCoin()
    }
    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }


    private fun handleNotData(listData: ArrayList<FavouriteModel>) {
        if (listData.size > 0) {
            binding.LllNotiFavourite.visibility = View.GONE
            for (mList in listData) {
                Log.d(ContentValues.TAG, "initData: ${mList.strMeal}")
            }
        } else {
            binding.LllNotiFavourite.visibility = View.VISIBLE
        }
    }

    private fun putBundle(favouriteData: FavouriteModel): Bundle {
        val bundle = Bundle()
        bundle.putString(
            "strMeal", mListSend[mAdapter.getListItem().indexOf(favouriteData)].strMeal
        )
        bundle.putString(
            "strCategoryThumb",
            mListSend[mAdapter.getListItem().indexOf(favouriteData)].strMealThumb
        )
        bundle.putString(
            "idCategory", mListSend[mAdapter.getListItem().indexOf(favouriteData)].idMeal
        )
        bundle.putString(
            "strCheck", mListSend[mAdapter.getListItem().indexOf(favouriteData)].strCheck
        )
        bundle.putString(
            "strScreen", "favourite"
        )
        return bundle
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun createSnackBar(v: View, favouriteData: FavouriteModel): Snackbar {
        val snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT)
        val customSnackView = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.addView(customSnackView)
        snackbarLayout.setPadding(0, 0, 0, 0)
        customSnackView.findViewById<TextView>(R.id.textView1).text =
            "Bạn muốn xoá công thức nấu món ${
                mListSend[mAdapter.getListItem().indexOf(favouriteData)].strMeal
            } khỏi mục yêu thích?"
        Glide.with(v).load(mListSend[mAdapter.getListItem().indexOf(favouriteData)].strMealThumb)
            .into(
                customSnackView.findViewById(R.id.imageView)
            )
        customSnackView.findViewById<Button>(R.id.btnOkSnackbar).setOnClickListener {
            viewModel.deleteDataFavourite(
                mListSend[mAdapter.getListItem().indexOf(favouriteData)].idMeal
            )
            initData()
            snackbar.dismiss()
        }
        return snackbar
    }
}