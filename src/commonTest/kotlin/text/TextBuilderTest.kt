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

class TextBuilderTest {

    @Test
    fun `should return empty string for empty block`() {
        assert(buildText {} == "")
    }

    @Test
    fun `should append single string with unary plus`() {
        assert(buildText { +"hello" } == "hello")
    }

    @Test
    fun `should append single char with unary plus`() {
        assert(buildText { +'x' } == "x")
    }

    @Test
    fun `should append multiple strings`() {
        val result = buildText {
            +"hello"
            +" "
            +"world"
        }
        assert(result == "hello world")
    }

    @Test
    fun `should append multiple chars`() {
        val result = buildText {
            +'a'
            +'b'
            +'c'
        }
        assert(result == "abc")
    }

    @Test
    fun `should mix strings and chars`() {
        val result = buildText {
            +"hello"
            +','
            +' '
            +"world"
            +'!'
        }
        assert(result == "hello, world!")
    }

    @Test
    fun `should handle multiline text`() {
        val result = buildText {
            +"line1\n"
            +"line2\n"
            +"line3"
        }
        assert(result == "line1\nline2\nline3")
    }

    @Test
    fun `should handle newline chars`() {
        val result = buildText {
            +"line1"
            +'\n'
            +"line2"
        }
        assert(result == "line1\nline2")
    }

    @Test
    fun `trimLastNewLine should remove trailing newline`() {
        val result = buildText {
            +"hello\n"
            trimLastNewLine()
        }
        assert(result == "hello")
    }

    @Test
    fun `trimLastNewLine should do nothing when no trailing newline`() {
        val result = buildText {
            +"hello"
            trimLastNewLine()
        }
        assert(result == "hello")
    }

    @Test
    fun `trimLastNewLine should do nothing on empty builder`() {
        val result = buildText {
            trimLastNewLine()
        }
        assert(result == "")
    }

    @Test
    fun `trimLastNewLine should only remove last newline`() {
        val result = buildText {
            +"line1\n"
            +"line2\n"
            trimLastNewLine()
        }
        assert(result == "line1\nline2")
    }

    @Test
    fun `should work with string interpolation`() {
        val name = "world"
        val result = buildText {
            +"hello $name"
        }
        assert(result == "hello world")
    }

    @Test
    fun `should handle conditional appending`() {
        val includeGreeting = true
        val result = buildText {
            if (includeGreeting) {
                +"hello "
            }
            +"world"
        }
        assert(result == "hello world")
    }

    @Test
    fun `should handle loop appending`() {
        val result = buildText {
            for (i in 1..3) {
                +"$i"
                if (i < 3) +","
            }
        }
        assert(result == "1,2,3")
    }

    @Test
    fun `should handle special characters`() {
        val result = buildText {
            +"\t"
            +"indented"
            +'\r'
            +'\n'
        }
        assert(result == "\tindented\r\n")
    }

}
