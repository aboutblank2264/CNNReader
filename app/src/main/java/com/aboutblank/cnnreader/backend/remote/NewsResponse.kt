package com.aboutblank.cnnreader.backend.remote

import com.aboutblank.cnnreader.backend.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>,
    val code: String,
    val message: String
)