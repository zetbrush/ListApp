package com.listfetch.impl.data

import com.listapp.core.common.AppDispatcher

internal class ListItemsRepoImpl(
    private val dispatchers: AppDispatcher,
    private val service: ListItemsService
) : ListRepo {
    override suspend fun fetchItems(): Result<List<ListItemDTO>> = with(dispatchers.ioDispatcher) {
        return@with runCatching {
            service.fetchItems()
        }
    }
}