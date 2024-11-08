package com.listfetch.impl.data

import com.google.gson.annotations.SerializedName
import com.listfetch.api.domain.ListItem

internal data class ListItemDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("listId") val listID: Int?,
    @SerializedName("name") val name: String?
)

internal fun ListItemDTO.toListItem() = ListItem(id = id, listID = listID, name = name)