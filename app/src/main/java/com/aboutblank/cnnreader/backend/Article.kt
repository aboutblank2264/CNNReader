package com.aboutblank.cnnreader.backend

import com.google.gson.annotations.SerializedName

data class Article(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    @SerializedName("urlToImage")
    val image: String,
    @SerializedName("publishedAt")
    val date: String,
    val content: String
)