package com.example.cookingguide.model.filter

import com.google.gson.annotations.SerializedName

data class CategoryDetails(
    @SerializedName("idMeal")
    val idMeal: String,
    @SerializedName("strMeal")
    val strMeal: String,
    @SerializedName("strMealThumb")
    val strMealThumb: String,
    val strCategoryThumb: String
)