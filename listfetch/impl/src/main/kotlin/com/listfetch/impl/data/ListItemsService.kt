package com.listfetch.impl.data
import retrofit2.http.GET

internal fun interface ListItemsService {
    @GET("hiring.json")
    suspend fun fetchItems(): List<ListItemDTO>
}