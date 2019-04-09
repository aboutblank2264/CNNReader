package com.aboutblank.cnnreader

import android.app.Application
import com.aboutblank.cnnreader.di.components.AppComponent
import com.aboutblank.cnnreader.di.components.DaggerAppComponent
import com.aboutblank.cnnreader.di.modules.BackendModule
import com.aboutblank.cnnreader.di.modules.UtilsModule

class NewsApplication : Application() {
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
}