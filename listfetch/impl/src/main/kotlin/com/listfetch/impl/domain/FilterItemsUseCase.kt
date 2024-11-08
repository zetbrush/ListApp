package com.listfetch.impl.domain

import com.listapp.core.common.AppDispatcher
import com.listfetch.api.domain.ListItem
import com.listfetch.impl.data.ListRepo
import com.listfetch.impl.data.toListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.listfetch.impl.presenter.Result
import kotlinx.coroutines.flow.flowOn

internal class FilterItemsUseCase(private val listRepo: ListRepo, private val dispatcher: AppDispatcher) {

    fun execute(): Flow<Result<Map<Int, List<ListItem>>>> = flow {
        emit(Result.Loading) // show loading

        try {
            val items = listRepo.fetchItems().getOrThrow()
                .filter { !it.name.isNullOrBlank() }
                .map { it.toListItem() }
                .sortedWith(compareBy({ it.listID }, { it.name }))
                .groupBy { it.listID ?: 0 }
            emit(Result.Success(items)) //success with data
        } catch (e: Throwable) {
            emit(Result.Error(e)) //error on failure
        }
    }.flowOn(dispatcher.defaultDispatcher)
}