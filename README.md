# xemantic-kotlin-core

Kotlin extensions which should have been added to the stdlib

[<img alt="Maven Central Version" src="https://img.shields.io/maven-central/v/com.xemantic.kotlin/xemantic-kotlin-core">](https://central.sonatype.com/artifact/com.xemantic.kotlin/xemantic-kotlin-core)
[<img alt="GitHub Release Date" src="https://img.shields.io/github/release-date/xemantic/xemantic-kotlin-core">](https://github.com/xemantic/xemantic-kotlin-core/releases)
[<img alt="license" src="https://img.shields.io/github/license/xemantic/xemantic-kotlin-core?color=blue">](https://github.com/xemantic/xemantic-kotlin-core/blob/main/LICENSE)

[<img alt="GitHub Actions Workflow Status" src="https://img.shields.io/github/actions/workflow/status/xemantic/xemantic-kotlin-core/build-main.yml">](https://github.com/xemantic/xemantic-kotlin-core/actions/workflows/build-main.yml)
[<img alt="GitHub branch check runs" src="https://img.shields.io/github/check-runs/xemantic/xemantic-kotlin-core/main">](https://github.com/xemantic/xemantic-kotlin-core/actions/workflows/build-main.yml)
[<img alt="GitHub commits since latest release" src="https://img.shields.io/github/commits-since/xemantic/xemantic-kotlin-core/latest">](https://github.com/xemantic/xemantic-kotlin-core/commits/main/)
[<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/xemantic/xemantic-kotlin-core">](https://github.com/xemantic/xemantic-kotlin-core/commits/main/)

[<img alt="GitHub contributors" src="https://img.shields.io/github/contributors/xemantic/xemantic-kotlin-core">](https://github.com/xemantic/xemantic-kotlin-core/graphs/contributors)
[<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/t/xemantic/xemantic-kotlin-core">](https://github.com/xemantic/xemantic-kotlin-core/commits/main/)
[<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/xemantic/xemantic-kotlin-core">]()
[<img alt="GitHub Created At" src="https://img.shields.io/github/created-at/xemantic/xemantic-kotlin-core">](https://github.com/xemantic/xemantic-kotlin-core/commits)
[<img alt="kotlin version" src="https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fxemantic%2Fxemantic-kotlin-core%2Fmain%2Fgradle%2Flibs.versions.toml&query=versions.kotlin&label=kotlin">](https://kotlinlang.org/docs/releases.html)
[<img alt="discord users online" src="https://img.shields.io/discord/811561179280965673">](https://discord.gg/vQktqqN2Vn)
[![Bluesky](https://img.shields.io/badge/Bluesky-0285FF?logo=bluesky&logoColor=fff)](https://bsky.app/profile/xemantic.com)

## Why?

This project collects code, which should be a part of the Kotlin stdlib, either soon or in general as a desired functionality.

## Usage

In `build.gradle.kts` add:

```kotlin
dependencies {
    implementation("com.xemantic.kotlin:xemantic-kotlin-core:0.2.0")
}
```

## Development

### Update gradlew wrapper

```shell
./gradlew wrapper --gradle-version 8.12.1 --distribution-type bin
```

### Update all the dependencies to the latest versions

All the gradle dependencies are managed by the [libs.versions.toml](gradle/libs.versions.toml) file in the `gradle` dir.

It is easy to check for the latest version by running:

```shell
./gradlew dependencyUpdates
```
