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

import com.xemantic.kotlin.test.assert
import com.xemantic.kotlin.test.sameAs
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LineFlowTest {

    @Test
    fun `should flow single line with trailing newline`() = runTest {
        // given
        val text = "hello"

        // when
        val lines = text.lineFlow().toList()

        // then
        assert(lines.size == 1)
        lines[0] sameAs "hello\n"
    }

    @Test
    fun `should flow multiple lines each with trailing newline`() = runTest {
        // given
        val text = "line1\nline2\nline3"

        // when
        val lines = text.lineFlow().toList()

        // then
        assert(lines.size == 3)
        lines[0] sameAs "line1\n"
        lines[1] sameAs "line2\n"
        lines[2] sameAs "line3\n"
    }

    @Test
    fun `should preserve empty lines`() = runTest {
        // given
        val text = "line1\n\nline3"

        // when
        val lines = text.lineFlow().toList()

        // then
        assert(lines.size == 3)
        lines[0] sameAs "line1\n"
        lines[1] sameAs "\n"
        lines[2] sameAs "line3\n"
    }

    @Test
    fun `should flow empty string as single empty line with newline`() = runTest {
        // given
        val text = ""

        // when
        val lines = text.lineFlow().toList()

        // then
        assert(lines.size == 1)
        lines[0] sameAs "\n"
    }

    @Test
    fun `should reconstruct original text plus trailing newline when joined`() = runTest {
        // given
        val text = "first\nsecond\nthird"

        // when
        val reconstructed = text.lineFlow().joinToString()

        // then
        reconstructed sameAs "first\nsecond\nthird\n"
    }

}