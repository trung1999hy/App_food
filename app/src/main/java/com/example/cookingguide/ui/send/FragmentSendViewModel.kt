package com.example.cookingguide.ui.send

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cookingguide.local.InstructionsHelper
import com.example.cookingguide.model.SeenModel

class FragmentSendViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var helper: InstructionsHelper

    init {
        doSomething()
    }

    private fun doSomething() {
        helper = InstructionsHelper(getApplication<Application>().applicationContext)
    }

    fun getDataSeen(): ArrayList<SeenModel> {
        return helper.getCategorySeen()
    }

    fun getIdMealSqlite(idMeal: String): String {
        return helper.getDataByIdMeals(idMeal)
    }
}