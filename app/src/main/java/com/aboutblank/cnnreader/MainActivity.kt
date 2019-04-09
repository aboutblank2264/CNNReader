package com.aboutblank.cnnreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aboutblank.cnnreader.backend.IImageService
import com.aboutblank.cnnreader.backend.INewsService
import com.aboutblank.cnnreader.backend.StatusEnum.ERROR
import com.aboutblank.cnnreader.backend.StatusEnum.OK
import com.aboutblank.cnnreader.ui.NewsAdapterListener
import com.aboutblank.cnnreader.ui.NewsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NewsAdapterListener {
    private val TAG = MainActivity::class.java.name

    @Inject
    lateinit var newsService: INewsService
    @Inject
    lateinit var imageService: IImageService

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        NewsApplication.appComponent.inject(this)

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.init(newsService, imageService)

        viewModel.observeStatus(this, Observer {
            if (it.status == OK) {
                swipeLayout.isRefreshing = false
                Log.d(TAG, "OK ${it.message}")
            } else if (it.status == ERROR) {
                Toast.makeText(
                    this,
                    "Something has gone wrong: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.observeArticles(this, Observer {
            (recyclerView.adapter as NewsRecyclerAdapter).update(it)
        })

        swipeLayout = main_swipe_container.apply {
            setOnRefreshListener {
                viewModel.refresh()
            }
        }

        recyclerView = main_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = NewsRecyclerAdapter(viewModel.type, this@MainActivity)
        }
    }

    override fun onClick(url: String) {
        Log.d(TAG, "Load URL $url")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onLoadImage(image: String, view: ImageView) {
        viewModel.loadImage(image, view)
    }
}
