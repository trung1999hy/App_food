package com.example.cookingguide.network

import com.example.cookingguide.model.categories.CategoryResponse
import com.example.cookingguide.model.filter.CategoryDetailResponse
import com.example.cookingguide.model.lookup.LookUpResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php?")
    suspend fun getCategoryDetail(@Query("c") strCategory : String) : CategoryDetailResponse

    @GET("lookup.php?")
    suspend fun getLookUpById(@Query("i") idFood : String) : LookUpResponse
}