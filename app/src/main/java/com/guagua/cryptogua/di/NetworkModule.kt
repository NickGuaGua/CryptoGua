package com.guagua.cryptogua.di

import com.guagua.cryptogua.model.market.remote.BtseAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.btse.com/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideBsteApi(retrofit: Retrofit): BtseAPI = retrofit.create(BtseAPI::class.java)

    @Provides
    @Singleton
    fun provideFuturesWsRequest() = Request.Builder()
        .url("wss://ws.btse.com/ws/futures")
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideJson() = Json { ignoreUnknownKeys = true }
}