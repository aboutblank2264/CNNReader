package com.aboutblank.cnnreader

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.aboutblank.cnnreader.di.components.AppComponent
import com.aboutblank.cnnreader.di.components.DaggerAppComponent
import com.aboutblank.cnnreader.di.modules.BackendModule
import com.aboutblank.cnnreader.di.modules.UtilsModule
import com.aboutblank.cnnreader.utils.IntentReceiver

class NewsApplication : Application(), IntentReceiver {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .backendModule(
                BackendModule(
                    getString(R.string.base_url),
                    getString(R.string.read_timeOut).toLong(),
                    getString(R.string.connect_timeOut).toLong()
                )
            )
            .utilsModule(UtilsModule(this@NewsApplication))
            .build()
    }

    override fun accept(intent: Intent) {
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}