package com.example.core.data.di

import com.example.core.data.repository.WeatherRepository
import com.example.core.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // @Binds 告訴 Hilt：當有人需要 WeatherRepository 時
    // 給他 WeatherRepositoryImpl 的實例
    // 這樣 ViewModel 只依賴 interface，不知道實作細節
    // 符合 Clean Architecture 的依賴反轉原則
    @Binds @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository
}