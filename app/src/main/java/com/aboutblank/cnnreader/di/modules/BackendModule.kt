package com.aboutblank.cnnreader.di.modules

import android.content.Context
import com.aboutblank.cnnreader.BuildConfig
import com.aboutblank.cnnreader.R
import com.aboutblank.cnnreader.backend.IImageService
import com.aboutblank.cnnreader.backend.INewsService
import com.aboutblank.cnnreader.backend.ImageService
import com.aboutblank.cnnreader.backend.NewsService
import com.aboutblank.cnnreader.backend.cache.CacheRepo
import com.aboutblank.cnnreader.backend.remote.CNNRemoteRepo
import com.aboutblank.cnnreader.utils.ArticleCache
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class BackendModule(
    private val baseUrl: String,
    private val readTimeOut: Long,
    private val connectTimeOut: Long
) {

    @Provides
    @Singleton
    fun provideImageService(): IImageService = ImageService()

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit, articleCache: ArticleCache): INewsService =
        NewsService(retrofit.create(CNNRemoteRepo::class.java), CacheRepo(articleCache))

    @Provides
    @Singleton
    fun provideArticleCache(context: Context, gson: Gson) =
        ArticleCache(context.getString(R.string.cacheName), context.cacheDir, gson)

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideInterceptor() = Interceptor { chain ->
        val request = chain.request()
        val newUrl = request
            .url()
            .newBuilder()
            .addQueryParameter("apiKey", BuildConfig.apiKey)
            .build()

        chain.proceed(
            request
                .newBuilder()
                .url(newUrl)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
            .readTimeout(readTimeOut, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .build()
}