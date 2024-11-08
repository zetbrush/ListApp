package com.listfetch.api

import androidx.compose.runtime.Composable

interface ListFetchApi {
    @Composable
    fun getListFetchComposableScreen(): @Composable () -> Unit
}