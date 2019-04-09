package com.aboutblank.cnnreader.backend.cache

import android.os.AsyncTask
import com.aboutblank.cnnreader.backend.Article

class RetrieveAsyncTask(val function: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        function()
        return null
    }

}

class CacheAsyncTask(private val articles: List<Article>, val function: (List<Article>) -> Unit) :
    AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        function(articles)
        return null
    }
}