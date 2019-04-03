package com.aboutblank.cnnreader.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.aboutblank.cnnreader.R

class RecyclerViewLayout : ConstraintLayout {
    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.recyclerViewLayoutStyle)
    constructor(context: Context) : super(context, null, R.attr.recyclerViewLayoutStyle)
}