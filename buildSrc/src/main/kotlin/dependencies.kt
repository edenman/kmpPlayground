@file:Suppress("ConstPropertyName")

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

val globalRepoList: RepositoryHandler.() -> Unit = {
  google()
  mavenCentral()
  maven("https://plugins.gradle.org/m2/")
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

object Versions {
  val javaVersion = JavaVersion.VERSION_17
  const val jvmTarget = "17"
  const val kotlinLanguageVersion = "1.9"
  const val compileSdkVersion = 34
  const val minSdkVersion = 26
  const val targetSdkVersion = 34

  const val androidxAnnotations = "1.3.0-alpha01"
  const val androidxAppcompat = "1.4.0-beta01"
  const val androidxComposeBOM = "2024.03.00"
  const val androidxComposeCompiler = "1.5.10"
  const val androidxComposeMaterial3 = "1.2.0"
  const val androidxCore = "1.7.0-alpha01"
  const val androidxEmoji2 = "1.0.0-beta01"
  const val androidxTestCore = "1.2.0"
  const val coil = "1.3.0"
  const val coreTesting = "2.1.0"
  const val junit = "4.13"
  const val kotlin = "1.9.22"
  const val kotlinSerialization = "1.6.2"
  const val kotlinCoroutines = "1.8.0-RC"
  const val ktor = "2.0.0-beta-1"
  const val material = "1.4.0-rc01"
  const val multiplatformSwiftPlugin = "2.0.3"
  const val okhttp = "4.10.0-RC1"
  const val phrase = "1.1.0"
  const val robolectric = "4.5-alpha-3"
  const val sqlDelight = "1.5.4"
  const val squareFlow = "1.0.0-alpha3"
  const val timber = "5.0.1"
  const val truth = "1.0"
  const val urlDetector = "0.1.23"
  const val xmlAPIs = "1.4.01"
}

const val JUnit = "junit:junit:${Versions.junit}"
const val Robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
const val Timber = "com.jakewharton.timber:timber:${Versions.timber}"
const val UrlDetector = "io.github.url-detector:url-detector:${Versions.urlDetector}"
const val XmlAPIs = "xml-apis:xml-apis:${Versions.xmlAPIs}"

object AndroidX {
  const val annotations = "androidx.annotation:annotation:${Versions.androidxAnnotations}"
  const val appcompat = "androidx.appcompat:appcompat:${Versions.androidxAppcompat}"
  const val composeBOM = "androidx.compose:compose-bom:${Versions.androidxComposeBOM}"
  const val composeUI = "androidx.compose.ui:ui"
  const val composeMaterial = "androidx.compose.material:material:${Versions.androidxComposeMaterial3}"
  const val composeAnimation = "androidx.compose.animation:animation"
  const val composeUITooling = "androidx.compose.ui:ui-tooling"
  const val composeFoundation = "androidx.compose.foundation:foundation"

  const val coreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
  const val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"
  const val emoji = "androidx.emoji2:emoji2:${Versions.androidxEmoji2}"
  const val emojiBundled = "androidx.emoji2:emoji2-bundled:${Versions.androidxEmoji2}"
  const val testCore = "androidx.test:core:${Versions.androidxTestCore}"
}

object Coil {
  const val main = "io.coil-kt:coil:${Versions.coil}"
  const val gif = "io.coil-kt:coil-gif:${Versions.coil}"
  const val svg = "io.coil-kt:coil-svg:${Versions.coil}"
  const val video = "io.coil-kt:coil-video:${Versions.coil}"
}

object Google {
  const val materialUI = "com.google.android.material:material:${Versions.material}"
  const val truth = "com.google.truth:truth:${Versions.truth}"
}

object Kotlin {
  const val coroutinesCore =
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  const val coroutinesAndroid =
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
  const val coroutinesTest =
    "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
  const val serializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}"
  const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
  const val stdlibJDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
}

object Ktor {
  const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
  const val websockets = "io.ktor:ktor-client-websockets:${Versions.ktor}"
  const val clientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
}

object Square {
  const val flow = "com.squareup.flow:flow:${Versions.squareFlow}"
  const val phrase = "com.squareup.phrase:phrase:${Versions.phrase}"
  const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
}

object SqlDelight {
  const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
  const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
  const val javascriptRuntime = "com.squareup.sqldelight:runtime-js:${Versions.sqlDelight}"
  const val jvmRuntime = "com.squareup.sqldelight:runtime-jvm:${Versions.sqlDelight}"
}
