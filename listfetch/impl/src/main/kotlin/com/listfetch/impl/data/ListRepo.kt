package com.listfetch.impl.data

internal interface ListRepo {
    suspend fun fetchItems() : Result<List<ListItemDTO>>
}