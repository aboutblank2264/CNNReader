package com.aboutblank.cnnreader.backend.cache

import com.aboutblank.cnnreader.backend.Article
import com.aboutblank.cnnreader.utils.ArticleCache

interface ICacheRepo {
    fun getArticles(): List<Article>?
    fun saveArticles(articles: List<Article>)
}

class CacheRepo(private val articleCache: ArticleCache) : ICacheRepo {

    override fun getArticles(): List<Article>? {
        return articleCache.retrieveArticlesFromCache()
    }

    override fun saveArticles(articles: List<Article>) {
        articleCache.writeArticlesToCache(articles)
    }
}