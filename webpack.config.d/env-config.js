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

// Browser bundles have no system environment, so the `env` implementation reads
// `process.env`, which does not exist in the browser. The webpack DefinePlugin
// below injects a `process.env` object holding the test variable, mirroring how
// xemantic-kotlin-test exposes environment variables to browser tests. The whole
// `process` object is replaced (rather than the `process.env.NAME` member) so
// that dynamic lookups - `process.env[name]` - resolve against it at runtime.
const webpack = require("webpack");
const envConfigPlugin = new webpack.DefinePlugin({
    "process": {
        "env": {
            "XEMANTIC_KOTLIN_CORE_TEST_ENV": JSON.stringify("xemantic-env-test-value")
        }
    }
});
config.plugins.push(envConfigPlugin);
