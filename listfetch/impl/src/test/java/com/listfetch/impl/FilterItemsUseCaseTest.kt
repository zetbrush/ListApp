package com.listfetch.impl

import com.listfetch.api.domain.ListItem
import com.listfetch.impl.data.ListRepo
import com.listfetch.impl.presenter.Result
import com.listfetch.impl.domain.FilterItemsUseCase
import com.listapp.core.common.AppDispatcher
import com.listfetch.impl.data.ListItemDTO
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class FilterItemsUseCaseTest : FreeSpec({

    val mockData = listOf(
        ListItemDTO(id = 1, listID = 1, name = "Item A"),
        ListItemDTO(id = 2, listID = 1, name = "Item B"),
        ListItemDTO(id = 3, listID = 2, name = "Item C"),
        ListItemDTO(id = 4, listID = 2, name = "") // This item should be filtered out
    )

    val listRepo = mockk<ListRepo>(relaxUnitFun = true)
    val testDispatcher = UnconfinedTestDispatcher()
    val appDispatcher = object : AppDispatcher {
        override val ioDispatcher: CoroutineDispatcher get() = testDispatcher
        override val mainDispatcher: CoroutineDispatcher get() = testDispatcher
        override val defaultDispatcher: CoroutineDispatcher get() = testDispatcher
    }

    val useCase = FilterItemsUseCase(listRepo, appDispatcher)


    "FilterItemsUseCase" - {

        "should emit Loading and Success with correctly filtered, sorted, and grouped items" {
            coEvery { listRepo.fetchItems() } returns kotlin.Result.success(mockData)

            runTest {
                val result = useCase.execute().toList()
                advanceUntilIdle()

                result[0] shouldBe Result.Loading
                // Expected sorted and grouped data
                val expectedData = mapOf(
                    1 to listOf(ListItem(1, 1, "Item A"), ListItem(2, 1, "Item B")),
                    2 to listOf(ListItem(3, 2, "Item C"))
                )
                result[1] shouldBe Result.Success(expectedData)
            }
        }

        "should emit Loading and Error when an exception is thrown" {
            coEvery { listRepo.fetchItems() } throws RuntimeException("Network error")

            runTest {
                val result = useCase.execute().toList()
                advanceUntilIdle()

                result[0] shouldBe Result.Loading
                result[1].shouldBeInstanceOf<Result.Error>()
                (result[1] as Result.Error).exception.shouldBeInstanceOf<RuntimeException>()
                (result[1] as Result.Error).exception.message shouldBe "Network error"
            }
        }
    }
})
