package com.listfetch.impl.di

import androidx.compose.runtime.Composable
import com.listfetch.api.ListFetchApi
import com.listfetch.impl.data.ListItemsRepoImpl
import com.listfetch.impl.data.ListItemsService
import com.listfetch.impl.data.ListRepo
import com.listfetch.impl.domain.FilterItemsUseCase
import com.listfetch.impl.presenter.ItemsScreenViewModel
import com.listfetch.impl.presenter.listscreen.ItemsScreen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val listFetchModule = module {
    single<ListItemsService> { get<Retrofit>().create(ListItemsService::class.java) }
    single<ListRepo> { ListItemsRepoImpl(dispatchers = get(), service = get()) }
    viewModel<ItemsScreenViewModel> {
        ItemsScreenViewModel(
            FilterItemsUseCase(
                listRepo = get(),
                dispatcher = get()
            ),
            androidContext()
        )
    }

    single<ListFetchApi> { object : ListFetchApi {
        @Composable
        override fun getListFetchComposableScreen(): @Composable () -> Unit {
            return {
                val viewModel: ItemsScreenViewModel = koinViewModel()
                ItemsScreen(viewModel = viewModel)
            }
        }
    } }
}