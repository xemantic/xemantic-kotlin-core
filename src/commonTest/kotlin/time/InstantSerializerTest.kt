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

@file:UseSerializers(InstantSerializer::class)

package com.xemantic.kotlin.core.time

import com.xemantic.kotlin.test.assert
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.time.Instant

class InstantSerializerTest {

    // given
    @Serializable
    data class Foo(
        val moment: Instant
    )

    @Test
    fun `should serialize class with Instant property`() {
        // given
        val foo = Foo(
            moment = Instant.parse("2025-06-25T19:04:26.781652Z")
        )

        // when
        val value = Json.encodeToString(foo)

        // then
        assert(value == """{"moment":"2025-06-25T19:04:26.781652Z"}""")
    }

    @Test
    fun `should deserialize class with Instant property`() {
        // given
        val json = """{"moment":"2025-06-25T19:04:26.781652Z"}"""

        // when
        val foo = Json.decodeFromString<Foo>(json)

        // then
        assert(foo.moment == Instant.parse("2025-06-25T19:04:26.781652Z"))
    }

}
