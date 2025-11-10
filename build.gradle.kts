@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.time.LocalDateTime

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.power.assert)
    alias(libs.plugins.kotlin.plugin.serialization) // used in tests
    alias(libs.plugins.kotlinx.binary.compatibility.validator)
    alias(libs.plugins.dokka)
    alias(libs.plugins.versions)
    alias(libs.plugins.maven.publish)
}

group = "com.xemantic.kotlin"

val now: LocalDateTime = LocalDateTime.now()
val metaOrganization = "Xemantic"
val metaOrganizationUrl = "https://xemantic.com"
val metaGitHub = "xemantic"
val metaInceptionYear = "2025"
val metaDescription = "Kotlin stdlib extensions"

fun MavenPomDeveloperSpec.projectDevs() {
    developer {
        id = "morisil"
        name = "Kazik Pogoda"
        url = "https://github.com/morisil"
    }
}

val isReleaseBuild: Boolean = !(project.version as String).endsWith("-SNAPSHOT")

val javaTarget = libs.versions.javaTarget.get()
val kotlinTarget = KotlinVersion.fromVersion(libs.versions.kotlinTarget.get())

kotlin {

    explicitApi()

    compilerOptions {
        apiVersion = kotlinTarget
        languageVersion = kotlinTarget
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xcontext-sensitive-resolution"
        )
        extraWarnings = true
        progressiveMode = true
        optIn.addAll("kotlin.time.ExperimentalTime")
    }

    jvm {
        // set up according to https://jakewharton.com/gradle-toolchains-are-rarely-a-good-idea/
        compilerOptions {
            apiVersion = kotlinTarget
            languageVersion = kotlinTarget
            jvmTarget = JvmTarget.fromTarget(javaTarget)
            freeCompilerArgs.add("-Xjdk-release=$javaTarget")
            progressiveMode = true
        }
    }

    js {
        browser()
        nodejs()
        binaries.library()
    }

    wasmJs {
        browser()
        nodejs()
        d8()
        binaries.library()
    }

    wasmWasi {
        nodejs()
        binaries.library()
    }

    // native, see https://kotlinlang.org/docs/native-target-support.html
    // tier 1
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()
    iosArm64()

    // tier 2
    linuxX64()
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()

    // tier 3
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    mingwX64()
    watchosDeviceArm64()

    swiftExport {}

    sourceSets {

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.xemantic.kotlin.test)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

    }

}

repositories {
    mavenCentral()
}

// skip tests which require XCode components to be installed
tasks {
    named("tvosSimulatorArm64Test") { enabled = false }
    named("watchosSimulatorArm64Test") { enabled = false }
}

powerAssert {
    functions = listOf(
        "com.xemantic.kotlin.test.assert",
        "com.xemantic.kotlin.test.have"
    )
}

dokka {
    pluginsConfiguration.html {
        val years = if (metaInceptionYear != now.year.toString()) {
            "$metaInceptionYear-${now.year}"
        } else {
            "${now.year}"
        }
        footerMessage = """© $years $metaOrganization"""
    }
}

//val isPublishingToGitHub = gradle.startParameter.taskNames.any {
//    it.contains("publishAllPublicationsToGitHubPackagesRepository")
//}

//publishing {
//    if (isPublishingToGitHub) {
//        repositories {
//            maven {
//                name = "GitHubPackages"
//                url = uri("https://maven.pkg.github.com/xemantic/xemantic-kotlin-core")
//                credentials(PasswordCredentials::class)
//            }
//        }
//    }
//}

mavenPublishing {

    configure(KotlinMultiplatform(
        javadocJar = JavadocJar.Dokka("dokkaGenerateHtml"),
        sourcesJar = true
    ))

    signAllPublications()

    publishToMavenCentral(
        automaticRelease = true
    )

    coordinates(
        groupId = group.toString(),
        artifactId = rootProject.name,
        version = version.toString()
    )

    pom {

        name = rootProject.name
        description = metaDescription
        inceptionYear = metaInceptionYear
        url = "https://github.com/$metaGitHub/${rootProject.name}"

        organization {
            name = metaOrganization
            url = metaOrganizationUrl
        }

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        scm {
            url = "https://github.com/$metaGitHub/${rootProject.name}"
            connection = "scm:git:git://github.com/$metaGitHub/${rootProject.name}.git"
            developerConnection = "scm:git:ssh://git@github.com/$metaGitHub/${rootProject.name}.git"
        }

        ciManagement {
            system = "GitHub"
            url = "https://github.com/$metaGitHub/${rootProject.name}/actions"
        }

        issueManagement {
            system = "GitHub"
            url = "https://github.com/$metaGitHub/${rootProject.name}/issues"
        }

        developers {
            projectDevs()
        }

    }

}

// dependencyUpdates task config
val unstableVersionKeywords = listOf("alpha", "beta", "rc")

fun isNonStableVersion(
    version: String
) = version.lowercase().let { normalizedVersion ->
    unstableVersionKeywords.any {
        it in normalizedVersion
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStableVersion(candidate.version)
    }
}
