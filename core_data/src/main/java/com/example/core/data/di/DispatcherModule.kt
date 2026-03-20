package com.example.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

// 自訂 annotation，讓 Hilt 知道這是 IO 用的 Dispatcher
// 避免跟其他 CoroutineDispatcher 衝突
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    // 提供 IO Dispatcher 給需要的地方注入
    // 這樣測試時可以換成 TestDispatcher，不用改程式碼
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}