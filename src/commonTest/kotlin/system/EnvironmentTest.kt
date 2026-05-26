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

import com.xemantic.kotlin.test.assert
import kotlin.test.Test

/**
 * The name of the environment variable provided to the test runners by the build
 * (`build.gradle.kts` for JVM/Node/native, `webpack.config.d/env-config.js` for
 * the browser). It must stay in sync with both of these locations.
 */
private const val PROVIDED_VARIABLE = "XEMANTIC_KOTLIN_CORE_TEST_ENV"

/**
 * The value the build assigns to [PROVIDED_VARIABLE].
 */
private const val PROVIDED_VALUE = "xemantic-env-test-value"

/**
 * A variable name that is guaranteed never to be defined in any test environment.
 */
private const val ABSENT_VARIABLE = "XEMANTIC_KOTLIN_CORE_TEST_ENV_THAT_IS_ABSENT"

class EnvironmentTest {

    // --- pure interface / extension contract, deterministic on every platform ---

    @Test
    fun `Environment should delegate lookup to the function it was created from`() {
        val environment = Environment { name -> if (name == "KEY") "value" else null }
        assert(environment["KEY"] == "value")
        assert(environment["MISSING"] == null)
    }

    @Test
    fun `get with default should return the value when the variable is present`() {
        val environment = Environment { "value" }
        assert(environment["KEY", "default"] == "value")
    }

    @Test
    fun `get with default should return the default when the variable is absent`() {
        val emptyEnvironment = Environment { null }
        assert(emptyEnvironment["KEY", "default"] == "default")
    }

    @Test
    fun `get with default should return an empty value rather than the default`() {
        val environment = Environment { "" }
        assert(environment["KEY", "default"] == "")
    }

    // --- platform `env` wiring ---

    @Test
    fun `env should return null for an undefined variable`() {
        assert(env[ABSENT_VARIABLE] == null)
    }

    @Test
    fun `env get with default should return the default for an undefined variable`() {
        assert(env[ABSENT_VARIABLE, "default"] == "default")
    }

    @Test
    fun `env should read the variable provided to the test runner`() {
        val value = env[PROVIDED_VARIABLE]
        // Environment variables are unavailable on Wasm/WASI and in the browser
        // unless explicitly wired up; there `env` returns null and there is
        // nothing to assert. Everywhere they are available (JVM, Node.js, native
        // and - via webpack - the browser) the build provides the value below.
        if (value != null) {
            assert(value == PROVIDED_VALUE)
            assert(env[PROVIDED_VARIABLE, "default"] == PROVIDED_VALUE)
        }
    }

}
