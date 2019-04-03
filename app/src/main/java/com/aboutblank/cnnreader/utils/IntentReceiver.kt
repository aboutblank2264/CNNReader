package com.aboutblank.cnnreader.utils

import android.content.Intent

interface IntentReceiver {
    fun accept(intent: Intent)
}