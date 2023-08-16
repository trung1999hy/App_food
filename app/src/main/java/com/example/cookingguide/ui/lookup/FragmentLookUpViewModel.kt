package com.example.cookingguide.ui.lookup

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cookingguide.local.InstructionsHelper
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.model.lookup.LookUpModel
import com.example.cookingguide.utils.Common
import kotlinx.coroutines.launch

class FragmentLookUpViewModel(application: Application) : AndroidViewModel(application) {
    private val _lookUp = MutableLiveData<ArrayList<LookUpModel>>()
    val lookUp : LiveData<ArrayList<LookUpModel>> = _lookUp
    private lateinit var helper: InstructionsHelper

    init {
        doSomething()
    }

    private fun doSomething() {
        helper = InstructionsHelper(getApplication<Application>().applicationContext)
    }

    fun fetchInstructions(i: String) {
        viewModelScope.launch {
            try {
                val instructions = Common.repository.getLookUp(i)
                _lookUp.postValue(instructions)
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "fetchMeals: $e")
            }
        }
    }

    fun inputDataSqlite(favouriteData: FavouriteModel) {
        helper.insertFavouriteNotExists(favouriteData)
    }

    fun deleteDataSqlite(idMeal: String) {
        helper.deleteDataFavourite(idMeal)
    }
}