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

package com.xemantic.kotlin.core.text

import com.xemantic.kotlin.test.sameAs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class JoinToStringTest {

    @Test
    fun `should join flow of strings into single string`() = runTest {
        // given
        val flow = flowOf("Hello", " ", "World", "!")

        // when
        val result = flow.joinToString()

        // then
        result sameAs "Hello World!"
    }

    @Test
    fun `should return empty string for empty flow`() = runTest {
        // given
        val flow = flowOf<String>()

        // when
        val result = flow.joinToString()

        // then
        result sameAs ""
    }

    @Test
    fun `should handle single element flow`() = runTest {
        // given
        val flow = flowOf("single")

        // when
        val result = flow.joinToString()

        // then
        result sameAs "single"
    }

}