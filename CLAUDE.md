# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform library providing extensions that should have been added to the Kotlin stdlib. The library is published to Maven Central and supports a comprehensive set of platforms including JVM, JS, Wasm, and numerous native targets (macOS, iOS, Linux, Windows, Android Native, watchOS, tvOS).

## Build Commands

### Building the project
```shell
./gradlew build
```

### Running tests
```shell
./gradlew test
```

### Running tests for a specific platform
```shell
./gradlew jvmTest                    # JVM tests
./gradlew jsTest                     # JS tests
./gradlew wasmJsTest                 # Wasm JS tests
./gradlew macosX64Test               # macOS x64 tests
./gradlew linuxX64Test               # Linux x64 tests
```

Note: tvosSimulatorArm64Test and watchosSimulatorArm64Test are disabled in the build configuration as they require XCode components.

### Generating API documentation
```shell
./gradlew dokkaGeneratePublicationHtml
```

### Publishing artifacts locally for testing
```shell
./gradlew publishToMavenLocal
```

### Checking for dependency updates
```shell
./gradlew dependencyUpdates
```

### Binary compatibility validation
```shell
./gradlew apiCheck                   # Check for API changes
./gradlew apiDump                    # Update API dump files
```

### Building all artifacts for publishing
```shell
./gradlew build sourcesJar javadocJar
```

## Architecture

### Module Structure

This is a single-module Kotlin Multiplatform library with the following source set structure:

- `src/commonMain/kotlin/` - Common Kotlin code for all platforms
- `src/commonTest/kotlin/` - Common test code

All source code resides in the `com.xemantic.kotlin.core` package namespace, with subpackages for different functionality domains (e.g., `collections`).

### Code Organization

The library is organized by functionality domains in separate packages:
- `com.xemantic.kotlin.core.collections` - Collection extensions (e.g., `mapLast`)
- Additional domains will follow the same pattern

Each extension function/class should:
- Be placed in its domain-specific package
- Have corresponding tests in the matching test package
- Use explicit API mode (all public APIs must have explicit visibility modifiers)

### Kotlin Multiplatform Setup

The project targets an extensive list of platforms configured in `build.gradle.kts`:

**JVM**: Configured with Java target 17 and Kotlin API version from `libs.versions.toml`

**JavaScript**: Both browser and Node.js, compiled as library

**WebAssembly**:
- wasmJs (browser, Node.js, d8)
- wasmWasi (Node.js)

**Native targets** organized by tier support:
- Tier 1: macOS (x64, Arm64), iOS (Simulator Arm64, x64, Arm64)
- Tier 2: Linux (x64, Arm64), watchOS, tvOS
- Tier 3: Android Native, mingwX64, watchOS device Arm64

### Compiler Configuration

The project uses advanced Kotlin compiler features:
- Progressive mode enabled
- Explicit API mode required
- Context parameters (`-Xcontext-parameters`)
- Context-sensitive resolution (`-Xcontext-sensitive-resolution`)
- Extra warnings enabled
- Power Assert plugin for better test assertions

### Testing Framework

Tests use `xemantic-kotlin-test` library with Power Assert integration. The Power Assert plugin is configured to work with:
- `com.xemantic.kotlin.test.assert` function
- `com.xemantic.kotlin.test.have` function

### Dependency Management

All dependencies are managed through `gradle/libs.versions.toml`:
- Kotlin version: Configured via `kotlinTarget` and `javaTarget` version properties
- Test dependencies: kotlin-test, xemantic-kotlin-test, kotlinx-serialization (for test serialization scenarios)

### Build Conventions

The project uses `com.xemantic.gradle.xemantic-conventions` plugin which handles:
- Maven publishing configuration
- POM generation with project metadata
- License management (Apache 2.0)
- Developer information

### Publishing

The project uses `com.vanniktech.maven.publish` plugin (version 0.34.0) for deployment:
- Maven Central deployment via Sonatype Central Portal
- Automated artifact handling for all Kotlin Multiplatform targets
- Automatic signing of all publications
- POM generation with complete metadata (licenses, developers, SCM)

Publishing tasks:
- `./gradlew publishToMavenCentral` - Publish to Maven Central (requires manual release)
- `./gradlew publishAndReleaseToMavenCentral` - Publish and automatically release
- `./gradlew publishToMavenLocal` - Publish to local Maven repository for testing

Required environment variables for publishing:
- `ORG_GRADLE_PROJECT_mavenCentralUsername` - Maven Central username
- `ORG_GRADLE_PROJECT_mavenCentralPassword` - Maven Central password
- `ORG_GRADLE_PROJECT_signingInMemoryKey` - GPG signing key
- `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword` - GPG key password

## File Headers

All Kotlin source files must include the Apache 2.0 license header:

```kotlin
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
```

## CI/CD

GitHub Actions workflows:
- `build-branch.yml` - Builds feature branches and PRs
- `build-main.yml` - Builds main branch and publishes SNAPSHOT to GitHub Packages
- `build-release.yml` - Builds releases and deploys to Maven Central
- `updater.yml` - Automated dependency updates

All workflows use:
- Java 24 (Temurin distribution)
- Ubuntu latest runner
- Gradle actions for caching