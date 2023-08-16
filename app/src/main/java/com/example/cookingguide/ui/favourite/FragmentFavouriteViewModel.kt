package com.example.cookingguide.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cookingguide.local.InstructionsHelper
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.model.SeenModel

class FragmentFavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var helper: InstructionsHelper

    init {
        doSomething()
    }

    private fun doSomething() {
        helper = InstructionsHelper(getApplication<Application>().applicationContext)
    }

    fun getDataFavourite(): ArrayList<FavouriteModel> {
        return helper.getDataFavourite()
    }

    fun deleteDataFavourite(idMeal: String) {
        helper.deleteDataFavourite(idMeal)
    }
}