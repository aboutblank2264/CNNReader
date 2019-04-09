package com.aboutblank.cnnreader

import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.aboutblank.cnnreader.backend.Article
import com.aboutblank.cnnreader.backend.IImageService
import com.aboutblank.cnnreader.backend.INewsService
import com.aboutblank.cnnreader.backend.Status
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val TAG = MainViewModel::class.java.name

    val type: Int = Random.nextInt(1, 3)
    lateinit var newsService: INewsService
    lateinit var imageService: IImageService
    private lateinit var articles: LiveData<List<Article>>
    private lateinit var status: LiveData<Status>

    fun init(newsService: INewsService, imageService: IImageService) {
        this.newsService = newsService
        this.imageService = imageService
        articles = newsService.articleLiveData
        status = newsService.statusLiveData

        newsService.getNews()
    }

    fun observeArticles(lifecycleOwner: LifecycleOwner, observer: Observer<List<Article>>) {
        articles.observe(lifecycleOwner, observer)
    }

    fun observeStatus(owner: LifecycleOwner, observer: Observer<Status>) {
        status.observe(owner, observer)
    }

    fun loadImage(url: String, view: ImageView) {
        imageService.loadImage(url, view)
    }

    fun refresh() {
        newsService.getNews()
    }
}