package com.listapp.core.di

import com.google.gson.GsonBuilder
import com.listapp.core.common.AppDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dispatcherModule = module {
    single<AppDispatcher> {
        object : AppDispatcher {
            override val ioDispatcher: CoroutineDispatcher
                get() = Dispatchers.IO
            override val mainDispatcher: CoroutineDispatcher
                get() = Dispatchers.Main
            override val defaultDispatcher: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }
}

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}