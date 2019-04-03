package com.aboutblank.cnnreader.di.components

import com.aboutblank.cnnreader.MainActivity
import com.aboutblank.cnnreader.di.modules.BackendModule
import com.aboutblank.cnnreader.di.modules.UtilsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BackendModule::class, UtilsModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}