package com.aboutblank.cnnreader.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aboutblank.cnnreader.R
import com.aboutblank.cnnreader.backend.Article

class NewsRecyclerAdapter(
    private val viewType: Int,
    private val newsAdapterListener: NewsAdapterListener,
    private val articles: MutableList<Article> = mutableListOf()
) :
    RecyclerView.Adapter<NewsRecyclerAdapter.NewsRecyclerViewHolder>() {

    private val TAG = NewsRecyclerAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRecyclerViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(getViewType(), parent, false)
        return NewsRecyclerViewHolder(layout)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: NewsRecyclerViewHolder, position: Int) {
        holder.update(articles[position])
    }

    fun update(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)

        notifyDataSetChanged()
    }

    private fun getViewType() = when (viewType) {
        1 -> R.layout.news_view_holder_type1
        2 -> R.layout.news_view_holder_type2
        else -> R.layout.news_view_holder_type3
    }

    inner class NewsRecyclerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var article: Article

        fun update(data: Article) {
            article = data
            when (getViewType()) {
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
            newsAdapterListener.onLoadImage(article.image, view.findViewById(R.id.image_view))

            view.setOnClickListener {
                newsAdapterListener.onClick(article.url)
            }
        }
    }
}

interface NewsAdapterListener {
    fun onClick(url: String)
    fun onLoadImage(image: String, view : ImageView)
}