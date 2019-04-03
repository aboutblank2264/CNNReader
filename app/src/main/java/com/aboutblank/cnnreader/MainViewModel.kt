package com.aboutblank.cnnreader

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.aboutblank.cnnreader.backend.Article
import com.aboutblank.cnnreader.backend.IImageService
import com.aboutblank.cnnreader.backend.INewsService
import com.aboutblank.cnnreader.backend.Status
import com.aboutblank.cnnreader.utils.IntentReceiver
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val TAG = MainViewModel::class.java.name

    val type: Int = Random.nextInt(1, 3)
    lateinit var newsService: INewsService
    lateinit var imageService: IImageService
    lateinit var intentReceiver: IntentReceiver
    private lateinit var articles: LiveData<List<Article>>
    private lateinit var status: LiveData<Status>

    fun init(newsService: INewsService, imageService: IImageService, intentReceiver: IntentReceiver) {
        this.newsService = newsService
        this.imageService = imageService
        this.intentReceiver = intentReceiver
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

    fun loadUrl(url: String) {
        Log.d(TAG, "Load URL $url")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        intentReceiver.accept(intent)
    }

    fun refresh() {
        newsService.getNews()
    }
}