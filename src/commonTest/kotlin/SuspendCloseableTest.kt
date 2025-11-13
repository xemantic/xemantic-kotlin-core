/*
 * Copyright 2025 Kazimierz Pogoda / Xemantic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xemantic.kotlin.core

import com.xemantic.kotlin.test.assert
import com.xemantic.kotlin.test.coroutines.should
import com.xemantic.kotlin.test.have
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SuspendCloseableTest {

    class TestResource(
        var closed: Boolean = false,
        val shouldThrowOnClose: Boolean = false
    ) : SuspendCloseable {
        override suspend fun close() {
            if (shouldThrowOnClose) {
                throw IllegalStateException("Close failed")
            }
            closed = true
        }
    }

    @Test
    fun `should close resource when use block completes successfully`() = runTest {
        // given
        val resource = TestResource()

        // when
        resource.use {
            assert(!it.closed)
        }

        // then
        assert(resource.closed)
    }

    @Test
    fun `should return value from use block`() = runTest {
        // given
        val resource = TestResource()

        // when
        val result = resource.use {
            "test result"
        }

        // then
        assert(result == "test result")
        assert(resource.closed)
    }

    @Test
    fun `should close resource when use block throws exception`() = runTest {
        // given
        val resource = TestResource()

        // when
        assertFailsWith<IllegalArgumentException> {
            resource.use {
                throw IllegalArgumentException("Block failed")
            }
        }

        // then
        assert(resource.closed)
    }

    @Test
    fun `should propagate exception from use block`() = runTest {
        // given
        val resource = TestResource()

        // when
        val exception = assertFailsWith<IllegalArgumentException> {
            resource.use {
                throw IllegalArgumentException("Test exception")
            }
        }

        // then
        exception should {
            have(exception.message == "Test exception")
        }
    }

    @Test
    fun `should add close exception as suppressed when both block and close throw`() = runTest {
        // given
        val resource = TestResource(shouldThrowOnClose = true)

        // when
        val exception = assertFailsWith<IllegalArgumentException> {
            resource.use {
                throw IllegalArgumentException("Block exception")
            }
        }

        // then
        exception should {
            have(message =="Block exception")
            have(suppressedExceptions.size == 1)
            have(suppressedExceptions[0] is IllegalStateException)
            have(suppressedExceptions[0].message == "Close failed")
        }

    }

    @Test
    fun `should propagate close exception when use block succeeds but close fails`() = runTest {
        // given
        val resource = TestResource(shouldThrowOnClose = true)

        // when
        val exception = assertFailsWith<IllegalStateException> {
            resource.use {
                @Suppress("UnusedExpression")
                "success"
            }
        }

        // then
        exception should {
            have(message == "Close failed")
        }
    }

    @Test
    fun `should handle null resource without throwing`() = runTest {
        // given
        val resource: TestResource? = null

        // when
        val result = resource.use {
            "test"
        }

        // then
        assert(result == "test")
    }

    @Test
    fun `should invoke use block exactly once`() = runTest {
        // given
        val resource = TestResource()
        var invocationCount = 0

        // when
        resource.use {
            invocationCount++
        }

        // then
        assert(invocationCount == 1)
    }

    @Test
    fun `should close resource even when it is null in finally block`() = runTest {
        // given
        var closeFinallyCalled = false
        val resource: TestResource? = null

        // when
        try {
            resource.use {
                // This should work fine with null
            }
            closeFinallyCalled = true
        } catch (e: Throwable) {
            // Should not throw
        }

        // then
        assert(closeFinallyCalled)
    }

    @Test
    fun `should allow calling suspend functions in use block`() = runTest {
        // given
        class SuspendResource : SuspendCloseable {
            var closed = false
            var operationCalled = false

            suspend fun performSuspendOperation() {
                kotlinx.coroutines.delay(10) // Simulate suspend operation
                operationCalled = true
            }

            override suspend fun close() {
                kotlinx.coroutines.delay(5) // Simulate async cleanup
                closed = true
            }
        }

        val resource = SuspendResource()

        // when
        val result = resource.use { res ->
            res.performSuspendOperation() // This should compile and work!
            "completed"
        }

        // then
        assert(resource.operationCalled)
        assert(resource.closed)
        assert(result == "completed")
    }

    @Test
    fun `should handle suspend exceptions and still close resource`() = runTest {
        // given
        class SuspendResource : SuspendCloseable {
            var closed = false

            suspend fun failingSuspendOperation() {
                kotlinx.coroutines.delay(10)
                throw IllegalStateException("Suspend operation failed")
            }

            override suspend fun close() {
                kotlinx.coroutines.delay(5)
                closed = true
            }
        }

        val resource = SuspendResource()

        // when
        val exception = assertFailsWith<IllegalStateException> {
            resource.use { res ->
                res.failingSuspendOperation()
            }
        }

        // then
        exception should {
            have(message == "Suspend operation failed")
        }
        assert(resource.closed)
    }

}
