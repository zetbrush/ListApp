package com.listfetch.impl.presenter

import android.content.Context
import androidx.lifecycle.ViewModel
import com.listapp.core.common.launchOnUi
import com.listfetch.api.domain.ListItem
import com.listfetch.impl.R
import com.listfetch.impl.domain.FilterItemsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


internal class ItemsScreenViewModel(private val processItemsUseCase: FilterItemsUseCase, private val appContext: Context) :
    ViewModel(){

    private val unknownErrorMessage: String by lazy {
        appContext.resources.getString(R.string.unknown_error)
    }
    // current state
    private val _itemsState = MutableStateFlow<Result<Map<Int, List<ListItem>>>>(Result.Loading)
    val itemsState: StateFlow<Result<Map<Int, List<ListItem>>>> = _itemsState

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    init {
        fetchAndProcessItems()
    }

    fun fetchAndProcessItems() = launchOnUi {
        processItemsUseCase.execute().collect { result ->
            when (result) {
                is Result.Success -> _itemsState.value = result
                is Result.Error -> {
                    _itemsState.value = result
                    _errorMessage.emit(result.exception.message ?: unknownErrorMessage)
                }

                is Result.Loading -> _itemsState.value = Result.Loading
            }
        }
    }
}