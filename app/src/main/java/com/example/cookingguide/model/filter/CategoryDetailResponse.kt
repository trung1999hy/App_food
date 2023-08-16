package com.example.cookingguide.model.filter

import com.google.gson.annotations.SerializedName

data class CategoryDetailResponse(
    @SerializedName("meals")
    val categoriesDetail: ArrayList<CategoryDetails>
)