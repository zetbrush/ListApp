package com.listapp.core.common

import kotlinx.coroutines.CoroutineDispatcher


interface AppDispatcher {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher
}