package com.example.watermatters.data

import androidx.annotation.DrawableRes

data class User(
    val userName: String,
    val drops: Int = 0,
    val bottles: Int = 0,
    @DrawableRes val icon: Int? = null,
)
