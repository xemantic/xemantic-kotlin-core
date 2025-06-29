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

package com.xemantic.kotlin.core.collections

import com.xemantic.kotlin.test.assert
import kotlin.test.Test

class MapLastTest  {

    @Test
    fun `should ignore transformation if the List is empty`() {
        assert(emptyList<String>().mapLast {
            "foo"
        } == emptyList<String>())
    }

    @Test
    fun `should transform last element in single-element list`() {
        assert(listOf("foo").mapLast {
            it.uppercase()
        } == listOf("FOO"))
    }

    @Test
    fun `should transform last element for multi-element list`() {
        assert(listOf("foo", "bar").mapLast {
            it.uppercase()
        } == listOf("foo", "BAR"))
    }

}
