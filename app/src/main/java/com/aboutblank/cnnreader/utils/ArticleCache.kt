package com.aboutblank.cnnreader.utils

import android.util.Log
import com.aboutblank.cnnreader.backend.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class ArticleCache(private val fileName: String, private val cacheDirectory: File, private val gson: Gson) {
    private val file = File(cacheDirectory, fileName)

    private fun getCache(): File {
        if (!file.exists()) {
            Log.d("CACHE TEST", "exists")
            File.createTempFile(fileName, null, cacheDirectory)
        }
        return file
    }

    fun writeArticlesToCache(articles: List<Article>) {
        BufferedWriter(FileWriter(getCache()), 1024).use {
            it.write(gson.toJson(articles))
        }
    }

    fun retrieveArticlesFromCache(): List<Article>? {
        if (!file.exists()) return null
        val strBuilder = StringBuilder()

        BufferedReader(InputStreamReader(FileInputStream(file))).use {
            var line = it.readLine()
            while (line != null) {
                strBuilder.append(line)
                line = it.readLine()
            }
        }

        return gson.fromJson(strBuilder.toString(), object : TypeToken<ArrayList<Article>>() {}.type)
    }
}