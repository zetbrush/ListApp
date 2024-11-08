package com.listapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import org.koin.android.ext.android.inject
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.listapp.theme.ListAppTheme
import com.listfetch.api.ListFetchApi

class MainActivity : ComponentActivity() {

    private val fetchApi: ListFetchApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        fetchApi.getListFetchComposableScreen().invoke()
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ListFetch() {
        ListAppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    fetchApi.getListFetchComposableScreen().invoke()
                }
            }
        }
    }


}

