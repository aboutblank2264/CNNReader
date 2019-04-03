package com.aboutblank.cnnreader

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aboutblank.cnnreader.backend.IImageService
import com.aboutblank.cnnreader.backend.INewsService
import com.aboutblank.cnnreader.backend.NewsService.Status
import com.aboutblank.cnnreader.ui.NewsRecyclerAdapter
import com.aboutblank.cnnreader.utils.IntentReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.name

    @Inject
    lateinit var newsService: INewsService
    @Inject
    lateinit var imageService: IImageService
    @Inject
    lateinit var intentReceiver: IntentReceiver

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        NewsApplication.appComponent.inject(this)

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.init(newsService, imageService, intentReceiver)

        viewModel.observeStatus(this, Observer {
            if (it == Status.OK) {
                swipeLayout.isRefreshing = false
            } else if (it == Status.ERROR) {
                Toast.makeText(this, "Something has gone wrong!", Toast.LENGTH_SHORT).show()
            }
        })

        swipeLayout = main_swipe_container.apply {
            setOnRefreshListener {
                viewModel.refresh()
            }
        }

        recyclerView = main_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = NewsRecyclerAdapter(this@MainActivity, mutableListOf(), viewModel)
        }
    }
}
