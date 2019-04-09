package com.aboutblank.cnnreader.di.modules

import android.content.Context
import com.aboutblank.cnnreader.NewsApplication
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule(private val application: NewsApplication) {

    @Singleton
    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}