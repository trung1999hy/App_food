package com.example.cookingguide.network

import com.example.cookingguide.model.categories.Categories
import com.example.cookingguide.model.filter.CategoryDetails
import com.example.cookingguide.model.lookup.LookUpModel

class Repository {
    private val apiService = RetrofitClient.create()

    suspend fun getCategory(): ArrayList<Categories> {
        return apiService.getCategories().categories
    }

    suspend fun getCategoryDetails(strCategory: String): ArrayList<CategoryDetails> {
        return apiService.getCategoryDetail(strCategory).categoriesDetail
    }

    suspend fun getLookUp(idFood: String): ArrayList<LookUpModel> {
        return apiService.getLookUpById(idFood).lookup
    }
}