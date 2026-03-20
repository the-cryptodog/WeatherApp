package com.example.core.data.di

import com.example.core.data.remote.OpenMeteoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // @Singleton 確保整個 App 只有一個 Moshi 實例
    // 避免重複建立，節省記憶體
    @Provides @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        // KotlinJsonAdapterFactory 讓 Moshi 支援 Kotlin data class
        // 自動把 JSON 欄位對應到 data class 的屬性
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        // Open-Meteo 的 base URL，注意最後要有斜線
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): OpenMeteoApiService =
    // Retrofit 根據 interface 自動產生實作
        // 你不需要自己寫 HTTP 請求的程式碼
        retrofit.create(OpenMeteoApiService::class.java)
}