package com.mashup.core.testing

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Test

class TurbineTest {
    @Test
    fun test_base_testCode() = runTest {
        flowOf("one", "two").test {
            assertEquals("one", awaitItem())
            assertEquals("two", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun test_only_first_item() = runTest {
        channelFlow {
            withContext(IO) {
                repeat(100) {
                    send("item $it")
                }
            }
        }.test {
            assertEquals("item 0", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_exception() = runTest {
        flow<Any> { throw RuntimeException("broken!") }.test {
            assertEquals("broken!", awaitError().message)
        }
    }

    @Test
    fun test_exception_in_order() = runTest {
        flow<Any> {
            emit("data is first")
            throw RuntimeException("broken!")
        }.test {
            assertEquals("broken!", awaitError().message)
            assertEquals("data is first", awaitItem())
        }
    }

    @Test
    fun test_delay() = runTest {
        flowOf("one", "two", "three")
            .map {
                delay(100)
                it
            }
            .test {
                // 0 - 100ms -> no emission yet
                // 100ms - 200ms -> "one" is emitted
                // 200ms - 300ms -> "two" is emitted
                // 300ms - 400ms -> "three" is emitted
                delay(250)
                assertEquals("two", expectMostRecentItem())
                cancelAndIgnoreRemainingEvents()
            }
    }
}