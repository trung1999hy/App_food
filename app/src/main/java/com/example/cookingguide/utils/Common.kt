package com.example.cookingguide.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cookingguide.R
import com.example.cookingguide.databinding.ToastCustomBinding
import com.example.cookingguide.network.Repository
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.regex.Pattern

object Common {
    val repository = Repository()
    var category: String? = null
    var categoryScreen: String? = null

    fun translateData(documents: String, resultStr: (String) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build()

        val translator: Translator = Translation.getClient(options)
        translator.downloadModelIfNeeded()
        translator.translate(documents)
            .addOnSuccessListener { translatedText ->
                Log.d("TranslatedText", translatedText)
                resultStr(translatedText)
            }
            .addOnFailureListener { e ->
                Log.e("TranslationError", "Translation failed.", e)
            }
    }

    fun download(ok: () -> Unit, err: () -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build()

        val translator: Translator = Translation.getClient(options)
        translator.downloadModelIfNeeded().addOnCompleteListener {
            ok.invoke()
        }.addOnFailureListener {
            err.invoke()
        }
    }

    fun openActivity(activity: Activity, destinationClass: Class<*>) {
        activity.startActivity(Intent(activity.application, destinationClass))
//        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
        activity.finish()
    }

    fun addFragment(activity: Activity, id: Int, fragment: Fragment) {
        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            .add(id, fragment)
            .commit()
    }

    fun extractVideoIdFromUrl(url: String): String? {
        val regex = ".*watch\\?v=(.{11}).*"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(url)
        if (matcher.matches()) {
            return matcher.group(1)
        }
        return null
    }

    fun replaceFragment(activity: Activity, id: Int, fragment: Fragment) {
        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.dim_in, R.anim.dim_out)
            .replace(id, fragment)
            .commit()
    }

    fun putDataBundle(activity: Activity, id: Int, fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.dim_in, R.anim.dim_out)
            .replace(id, fragment)
            .commit()
    }

    fun createCustomToast(
        activity: Activity,
        message: String,
        layoutInflater: LayoutInflater,
    ) {
        val toast = Toast(activity)
        toast.apply {
            val mBinding = ToastCustomBinding.inflate(layoutInflater)
            mBinding.tvMessageCustomToast.text = message
            duration = Toast.LENGTH_SHORT
            view = mBinding.root
            show()
        }
    }
}