package com.aboutblank.cnnreader.backend.remote

import retrofit2.Call
import retrofit2.http.GET

interface CNNRemoteRepo {

    @GET("top-headlines?sources=cnn")
    fun getNews(): Call<NewsResponse>
}