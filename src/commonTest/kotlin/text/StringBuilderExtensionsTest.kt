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
import kotlin.test.Test

class StringBuilderExtensionsTest {

    @Test
    fun `should append single string with unary plus`() {
        // when
        val result = buildString { +"hello" }

        // then
        assert(result == "hello")
    }

    @Test
    fun `should append single char with unary plus`() {
        // when
        val result = buildString { +'x' }

        // then
        assert(result == "x")
    }

    @Test
    fun `should append multiple strings`() {
        // when
        val result = buildString {
            +"hello"
            +" "
            +"world"
        }

        // then
        assert(result == "hello world")
    }

    @Test
    fun `should append multiple chars`() {
        // when
        val result = buildString {
            +'a'
            +'b'
            +'c'
        }

        // then
        assert(result == "abc")
    }

    @Test
    fun `should mix strings and chars`() {
        // when
        val result = buildString {
            +"hello"
            +','
            +' '
            +"world"
            +'!'
        }

        // then
        assert(result == "hello, world!")
    }

    @Test
    fun `should handle multiline text`() {
        // when
        val result = buildString {
            +"line1\n"
            +"line2\n"
            +"line3"
        }

        // then
        assert(result == "line1\nline2\nline3")
    }

    @Test
    fun `should handle newline chars`() {
        // when
        val result = buildString {
            +"line1"
            +'\n'
            +"line2"
        }

        // then
        assert(result == "line1\nline2")
    }

    @Test
    fun `trimLastNewLine should remove trailing newline`() {
        // when
        val result = buildString {
            +"hello\n"
            trimLastNewLine()
        }

        // then
        assert(result == "hello")
    }

    @Test
    fun `trimLastNewLine should do nothing when no trailing newline`() {
        // when
        val result = buildString {
            +"hello"
            trimLastNewLine()
        }

        // then
        assert(result == "hello")
    }

    @Test
    fun `trimLastNewLine should do nothing on empty builder`() {
        // when
        val result = buildString {
            trimLastNewLine()
        }

        // then
        assert(result == "")
    }

    @Test
    fun `trimLastNewLine should only remove last newline`() {
        // when
        val result = buildString {
            +"line1\n"
            +"line2\n"
            trimLastNewLine()
        }

        // then
        assert(result == "line1\nline2")
    }

    @Test
    fun `should work with string interpolation`() {
        // given
        val name = "world"

        // when
        val result = buildString {
            +"hello $name"
        }

        // then
        assert(result == "hello world")
    }

    @Test
    fun `should handle conditional appending`() {
        // given
        val includeGreeting = true

        // when
        val result = buildString {
            if (includeGreeting) {
                +"hello "
            }
            +"world"
        }

        // then
        assert(result == "hello world")
    }

    @Test
    fun `should handle loop appending`() {
        // when
        val result = buildString {
            for (i in 1..3) {
                +"$i"
                if (i < 3) +","
            }
        }

        // then
        assert(result == "1,2,3")
    }

    @Test
    fun `should handle special characters`() {
        // when
        val result = buildString {
            +"\t"
            +"indented"
            +'\r'
            +'\n'
        }

        // then
        assert(result == "\tindented\r\n")
    }

}
