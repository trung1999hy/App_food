package com.example.cookingguide.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingguide.model.categories.Categories
import com.example.cookingguide.utils.Common
import kotlinx.coroutines.launch

class FragmentHomeViewModel : ViewModel() {
    private val _categories = MutableLiveData<ArrayList<Categories>>()
    val categories: LiveData<ArrayList<Categories>> = _categories

    init {
        fetchCategory()
    }

    private fun fetchCategory() {
        viewModelScope.launch {
            try {
                val categoryList = Common.repository.getCategory()
                _categories.postValue(categoryList)
            } catch (e: Exception) {
                Log.d(TAG, "fetchMeals: $e")
            }
        }
    }
}