package com.listfetch.impl.presenter.listscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.listfetch.api.domain.ListItem
import com.listfetch.impl.R
import com.listfetch.impl.presenter.ItemsScreenViewModel
import com.listfetch.impl.presenter.Result

@Composable
internal fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ItemList(items: Map<Int, List<ListItem>>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (listId, itemList) ->
            // Display the List ID as a header
            item {
                Text(
                    text = "List ID: $listId",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color = Color.Yellow)
                )
            }
            // Display each itemList as a horizontal scrolling list
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(itemList.size) { item ->
                        ItemRow(itemList[item])
                    }
                }
            }
        }
    }
}

@Composable
internal fun ItemRow(item: ListItem) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(4.dp)
            .background(color = colorScheme.secondaryContainer)

    ) {
        Text(
            text = item.name ?: "",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
internal fun ErrorView(errorMessage: String, onRetry: () -> Unit) {
    val retry = stringResource(id = R.string.retry)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = errorMessage,
                color = colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text(retry)
            }
        }
    }
}


@Composable
internal fun ItemsScreen(viewModel: ItemsScreenViewModel) {
    val itemsState by viewModel.itemsState.collectAsState()
    val appContext = LocalContext.current.applicationContext
    val errMessage = stringResource(id = R.string.error_message)

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage.collect { errorMessage ->
            Toast.makeText(appContext, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    when (itemsState) {
        is Result.Loading -> {
            LoadingView()
        }
        is Result.Success -> {
            ItemList(items = (itemsState as Result.Success<Map<Int, List<ListItem>>>).data)
        }
        is Result.Error -> {
            ErrorView(
                errorMessage = errMessage,
                onRetry = { viewModel.fetchAndProcessItems() }
            )
        }
    }
}

