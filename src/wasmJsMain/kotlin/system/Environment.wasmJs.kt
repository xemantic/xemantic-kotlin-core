/*
 * Copyright 2026 Kazimierz Pogoda / Xemantic
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

package com.xemantic.kotlin.core.system

// `process` only exists on Node.js; on the browser there is no concept of
// environment variables, so we degrade gracefully and return `null`.
// Unlike the JS target, Wasm/JS has no `dynamic` type, so the lookup happens
// inside the `js(...)` snippet (with `name` passed through) rather than via
// Kotlin index access on a dynamic `process.env`.
public actual val env: Environment = Environment { name -> getEnv(name) }

@OptIn(ExperimentalWasmJsInterop::class)
private fun getEnv(name: String): String? =
    js("(typeof process !== 'undefined' && process.env && process.env[name] != null) ? process.env[name] : null")
