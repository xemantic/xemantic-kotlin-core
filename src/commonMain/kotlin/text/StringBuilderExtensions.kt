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

@Suppress("NOTHING_TO_INLINE")
context(builder: StringBuilder)
public inline operator fun CharSequence.unaryPlus() {
    builder.append(this)
}

@Suppress("NOTHING_TO_INLINE")
context(builder: StringBuilder)
public inline operator fun Char.unaryPlus() {
    builder.append(this)
}

context(builder: StringBuilder)
public fun trimLastNewLine() {
    if (builder.isNotEmpty() && builder.last() == '\n') {
        builder.deleteAt(builder.lastIndex)
    }
}
