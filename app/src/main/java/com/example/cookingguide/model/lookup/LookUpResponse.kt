package com.example.cookingguide.model.lookup

import com.google.gson.annotations.SerializedName

data class LookUpResponse(
    @SerializedName("meals")
    val lookup: ArrayList<LookUpModel>
)