package com.listapp.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.launchOnUi(action: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(Dispatchers.Main.immediate) { action() }