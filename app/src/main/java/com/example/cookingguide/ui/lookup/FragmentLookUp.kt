package com.example.cookingguide.ui.lookup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.cookingguide.R
import com.example.cookingguide.base.BaseFragmentWithBinding
import com.example.cookingguide.databinding.FragmentLookUpBinding
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.ui.MainApp
import com.example.cookingguide.ui.inapp.PurchaseInAppActivity
import com.example.cookingguide.utils.Common
import com.example.cookingguide.utils.Common.category
import com.example.cookingguide.utils.Common.categoryScreen
import com.example.cookingguide.utils.Common.createCustomToast

class FragmentLookUp : BaseFragmentWithBinding<FragmentLookUpBinding>(
    FragmentLookUpBinding::inflate
) {
    companion object {
        fun newInstance() = FragmentLookUp()
    }

    private val viewModel: FragmentLookUpViewModel by viewModels()
    private var idVideo: String = ""
    private var idDocs: String = ""
    private var checkClick = 0
    private var idMeal: String = ""
    private var strMeal: String = ""
    private var strThumb: String = ""

    @SuppressLint("SetJavaScriptEnabled", "SuspiciousIndentation")
    override fun initAction() {
        binding.ProgressLoadLook.visibility = View.VISIBLE
        Glide.with(requireActivity()).load(arguments?.getString("strCategoryThumb"))
            .into(binding.ImageTitleLookUp)
        Common.translateData(arguments?.getString("strMeal").toString()) { result ->
            binding.TextTitleLookUp.text = result
        }
        binding.coin.setOnClickListener {
            startActivity(Intent( requireActivity(),PurchaseInAppActivity::class.java))
        }
        categoryScreen = arguments?.getString("strScreen")
        Log.d(TAG, "initAction: ${arguments?.getString("strCheck")}")
        if (arguments?.getString("strCheck") == "favourite")
            binding.ImageFavorite.setImageResource(R.drawable.favorite)
        else
            binding.ImageFavorite.setImageResource(R.drawable.favorite_border)

        binding.ImageVideoIns.setOnClickListener {
            if (idVideo != "") {
                if (binding.RlVideo.visibility == View.GONE) {
                    setAnimImportVidIns(binding.RlVideo, 1f, 0f, 0f, -1f, true)
                    val id = Common.extractVideoIdFromUrl(idVideo)
                    val video =
                        "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$id\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
                    binding.VideoInstruction.loadData(video, "text/html", "utf-8")
                    binding.VideoInstruction.settings.javaScriptEnabled = true
                    binding.VideoInstruction.webChromeClient = WebChromeClient()
                }
            } else {
                createCustomToast(
                    requireActivity(),
                    "Hiện tại món ăn này chưa có video hướng dẫn",
                    layoutInflater
                )
            }
        }

        binding.ImageExitVideo.setOnClickListener {
            if (binding.RlVideo.visibility == View.VISIBLE)
                setAnimImportVidIns(binding.RlVideo, 0f, 1f, -1f, 0f, false)
        }

        binding.ImageFavorite.setOnClickListener {
            if (arguments?.getString("strCheck") == "favourite") {
                checkClick++
                viewModel.deleteDataSqlite(arguments?.getString("idCategory").toString())
                binding.ImageFavorite.setImageResource(R.drawable.favorite_border)
                if (checkClick == 2) {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setMessage("Bạn có muốn thêm  tốn 1 vàng không ?")
                        .setTitle("Thêm vào yêu thích ?")
                    dialog.setPositiveButton("Oke") { dialog, which ->
                        MainApp.getInstant()?.preference?.apply {
                            if (getValueCoin() > 0) {
                                setValueCoin(getValueCoin() - 1)
                                Toast.makeText(
                                    requireContext(),
                                    "Đã thêm  thành công và trù 1 vàng",
                                    Toast.LENGTH_SHORT

                                ).show()
                                binding.ImageFavorite.setImageResource(R.drawable.favorite)
                                viewModel.inputDataSqlite(setDataSqlite("favourite"))
                                getCoin()
                            } else startActivity(
                                Intent(
                                    requireActivity(),
                                    PurchaseInAppActivity::class.java
                                )
                            )
                        }
                    }
                    dialog.show()
                    checkClick = 0
                }
            } else {
                checkClick++
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setMessage("Bạn có muốn thêm  tốn 1 vàng không ?")
                    .setTitle("Thêm vào yêu thích ?")
                dialog.setPositiveButton("Oke") { dialog, which ->
                    MainApp.getInstant()?.preference?.apply {
                        if (getValueCoin() > 0) {
                            setValueCoin(getValueCoin() - 1)
                            Toast.makeText(
                                requireContext(),
                                "Đã thêm  thành công và trù 1 vàng",
                                Toast.LENGTH_SHORT

                            ).show()
                            binding.ImageFavorite.setImageResource(R.drawable.favorite)

                            viewModel.inputDataSqlite(setDataSqlite("favourite"))
                            getCoin()
                        } else startActivity(
                            Intent(
                                requireActivity(),
                                PurchaseInAppActivity::class.java
                            )
                        )
                    }
                }
                dialog.show()
                if (checkClick == 2) {
                    binding.ImageFavorite.setImageResource(R.drawable.favorite_border)
                    viewModel.deleteDataSqlite(arguments?.getString("idCategory").toString())
                    checkClick = 0
                }
            }
        }
    }

    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }


    override fun initData() {
        viewModel.lookUp.observe(viewLifecycleOwner, Observer {
            for (list in it) {
                category = list.strCategory
                Common.translateData(list.strInstructions) { result ->
                    binding.TextInstructions.text = result
                    binding.ProgressLoadLook.visibility = View.GONE
                }
                idVideo = list.strYoutube
                idDocs = list.strSource
                idMeal = list.idMeal
                strMeal = list.strMeal
                strThumb = list.strMealThumb
            }
        })
        viewModel.fetchInstructions(arguments?.getString("idCategory").toString())
        getCoin()
    }

    private fun setDataSqlite(check: String): FavouriteModel {
        return FavouriteModel(
            idMeal = idMeal,
            strMeal = strMeal,
            strMealThumb = strThumb,
            strCategoryThumb = arguments?.getString("strThumb").toString(),
            strCheck = check
        )
    }

    private fun setAnimImportVidIns(
        view: View,
        fromX: Float,
        toX: Float,
        fromX_: Float,
        toX_: Float,
        isCheck: Boolean,
    ) {
        val fadeAnimation = AlphaAnimation(0f, 1f)
        fadeAnimation.duration = 1500

        val initialTranslationY = view.translationY
        val initialTranslationY_ = binding.ImageTitleLookUp.translationY
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, fromX,
            Animation.RELATIVE_TO_SELF, toX,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        translateAnimation.duration = 1500
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeAnimation)
        animationSet.addAnimation(translateAnimation)

        val translateAnimation_ = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, fromX_,
            Animation.RELATIVE_TO_SELF, toX_,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        translateAnimation_.duration = 1500
        val animationSet_ = AnimationSet(true)
        animationSet_.addAnimation(fadeAnimation)
        animationSet_.addAnimation(translateAnimation_)

        view.startAnimation(animationSet)
        binding.ImageTitleLookUp.startAnimation(animationSet_)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                view.translationY = initialTranslationY
                binding.ImageTitleLookUp.translationY = initialTranslationY_
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        if (isCheck) {
            view.visibility = View.VISIBLE
            Handler().postDelayed({
                binding.ImageTitleLookUp.alpha = 0f
            }, 1500)
        } else {
            binding.ImageTitleLookUp.alpha = 1f
            Handler().postDelayed({
                view.visibility = View.GONE
                binding.VideoInstruction.loadData("", "text/html", "utf-8")
            }, 1500)
        }
    }
}