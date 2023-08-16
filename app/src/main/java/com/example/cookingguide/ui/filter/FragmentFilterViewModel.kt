package com.example.cookingguide.ui.filter

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cookingguide.local.InstructionsHelper
import com.example.cookingguide.model.filter.CategoryDetails
import com.example.cookingguide.utils.Common
import kotlinx.coroutines.launch

class FragmentFilterViewModel(application: Application) : AndroidViewModel(application) {
    private val _filter = MutableLiveData<ArrayList<CategoryDetails>>()
    val filter: LiveData<ArrayList<CategoryDetails>> = _filter
    private lateinit var helper: InstructionsHelper

    init {
        doSomething()
    }

    private fun doSomething() {
        helper = InstructionsHelper(getApplication<Application>().applicationContext)
    }

    fun fetchCategoryDetails(c: String) {
        viewModelScope.launch {
            try {
                val categoryDetailsList = Common.repository.getCategoryDetails(c)
                _filter.postValue(categoryDetailsList)
            } catch (e: Exception) {
                Log.d(TAG, "fetchMeals: $e")
            }
        }
    }

    fun inputDataSqlite(categoryDetails: CategoryDetails) {
        helper.insertSeenIfNotExists(categoryDetails)
    }

    fun getIdMealSqlite(idMeal: String): String {
        return helper.getDataByIdMeals(idMeal)
    }
}