package com.aboutblank.cnnreader.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.aboutblank.cnnreader.MainViewModel
import com.aboutblank.cnnreader.R
import com.aboutblank.cnnreader.backend.Article

class NewsRecyclerAdapter
constructor(context: Context, private val articles: MutableList<Article>, private val mainViewModel: MainViewModel) :
    RecyclerView.Adapter<NewsRecyclerAdapter.NewsRecyclerViewHolder>() {

    private val TAG = NewsRecyclerAdapter::class.java.name

    init {
        Log.d(TAG, "View set to type ${mainViewModel.type}")
        mainViewModel.observeArticles(context as LifecycleOwner, Observer { list ->
            update(list)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRecyclerViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(getViewType(), parent, false)
        return NewsRecyclerViewHolder(layout)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: NewsRecyclerViewHolder, position: Int) {
        holder.update(articles[position])
    }

    private fun update(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)

        notifyDataSetChanged()
    }

    private fun getViewType() = when (mainViewModel.type) {
        1 -> R.layout.news_view_holder_type1
        2 -> R.layout.news_view_holder_type2
        else -> R.layout.news_view_holder_type3
    }

    inner class NewsRecyclerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var article: Article

        fun update(data: Article) {
            article = data
            when (mainViewModel.type) {
                1 -> {
                    view.findViewById<TextView>(R.id.date_text).text =
                        article.date.substring(0, article.date.indexOf("T"))
                    view.findViewById<TextView>(R.id.summary_text).text = article.description
                }
                2 -> {
                    view.findViewById<TextView>(R.id.summary_text).text = article.description
                }
            }

            view.findViewById<TextView>(R.id.title_text).text = article.title
            mainViewModel.loadImage(article.image, view.findViewById(R.id.image_view))

            view.setOnClickListener {
                mainViewModel.loadUrl(article.url)
            }
        }
    }
}