package com.aboutblank.cnnreader.backend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aboutblank.cnnreader.backend.cache.CacheAsyncTask
import com.aboutblank.cnnreader.backend.cache.CacheRepo
import com.aboutblank.cnnreader.backend.cache.RetrieveAsyncTask
import com.aboutblank.cnnreader.backend.remote.CNNRemoteRepo
import com.aboutblank.cnnreader.backend.remote.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface INewsService {
    var statusLiveData: MutableLiveData<Status>
    var articleLiveData: MutableLiveData<List<Article>>
    fun getNews()
}

class NewsService(private val remoteRepo: CNNRemoteRepo, private val cacheRepo: CacheRepo) : INewsService {
    private val TAG = NewsService::class.java.name

    override var statusLiveData: MutableLiveData<Status> = MutableLiveData()
    override var articleLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    init {
        statusLiveData.postValue(Status(StatusEnum.LOADING))
    }

    override fun getNews() {
        Log.d(TAG, "Fetching news")
        statusLiveData.postValue(Status(StatusEnum.LOADING))

        remoteRepo.getNews().enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                RetrieveAsyncTask(::getCachedNews).execute()
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body()!!.status == "ok") {
                    articleLiveData.postValue(response.body()!!.articles)
                    statusLiveData.postValue(Status(StatusEnum.OK, response.message()))

                    CacheAsyncTask(response.body()!!.articles, ::cacheNews).execute()
                } else {
                    RetrieveAsyncTask(::getCachedNews).execute()
                }
            }
        })
    }

    private fun getCachedNews() {
        Log.d(TAG, "Unable to reach news service, attempting to retrieve cache")
        val retrieved = cacheRepo.getArticles()
        if (retrieved == null) {
            statusLiveData.postValue(Status(StatusEnum.ERROR, "Unable to retrieve cached articles"))
            Log.d(TAG, "Unable to retrieve cached articles")
        } else {
            articleLiveData.postValue(retrieved)
            statusLiveData.postValue(Status(StatusEnum.OK, "Retrieved cached articles"))
            Log.d(TAG, "Retrieved cached articles")
        }
    }

    private fun cacheNews(articles: List<Article>) {
        Log.d(TAG, "Attempting to cache articles")
        cacheRepo.saveArticles(articles)
    }
}