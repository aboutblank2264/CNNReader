package com.aboutblank.cnnreader.backend

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

interface IImageService {
    fun loadImage(url: String, view: ImageView)
}

class ImageService : IImageService {

    override fun loadImage(url: String, view: ImageView) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.overrideOf(550, 310))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(view)
    }
}