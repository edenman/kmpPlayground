import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

val globalRepoList: RepositoryHandler.() -> Unit = {
  google()
  jcenter()
  maven("https://plugins.gradle.org/m2/")
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

object Versions {
  const val compileSdkVersion = 29
  const val minSdkVersion = 28
  const val targetSdkVersion = 29

  const val androidxAnnotations = "1.1.0"
  const val androidxAppcompat = "1.1.0"
  const val androidXEmoji = "1.1.0"
  const val coil = "0.11.0"
  const val junit = "4.13"
  const val kotlin = "1.4.0-rc"
  const val kotlinSerialization = "1.0-M1-1.4.0-rc"
  const val kotlinCoroutines = "1.3.8-1.4.0-rc"
  const val ktor = "1.3.2-1.4.0-rc"
  const val ktx = "1.3.0"
  const val material = "1.3.0-alpha02"
  const val okhttp = "4.8.0"
  const val phrase = "1.1.0"
  const val sqlDelight = "1.3.0"
  const val squareFlow = "1.0.0-alpha3"
  const val timber = "4.7.1"
  const val urlDetector = "0.1.23"
}

const val Timber = "com.jakewharton.timber:timber:${Versions.timber}"
const val UrlDetector = "io.github.url-detector:url-detector:${Versions.urlDetector}"

object AndroidX {
  const val annotations = "androidx.annotation:annotation:${Versions.androidxAnnotations}"
  const val appcompat = "androidx.appcompat:appcompat:${Versions.androidxAppcompat}"
  const val coreKtx = "androidx.core:core-ktx:${Versions.ktx}"
  const val emoji = "androidx.emoji:emoji-bundled:${Versions.androidXEmoji}"
}

object Coil {
  const val main = "io.coil-kt:coil:${Versions.coil}"
  const val gif = "io.coil-kt:coil-gif:${Versions.coil}"
  const val svg = "io.coil-kt:coil-svg:${Versions.coil}"
  const val video = "io.coil-kt:coil-video:${Versions.coil}"
}

object Google {
  const val materialUI = "com.google.android.material:material:${Versions.material}"
}

object Kotlin {
  const val coroutinesCore =
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  const val coroutinesAndroid =
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
  const val serializationRuntime =
    "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinSerialization}"
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
  const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
  const val javascriptRuntime = "com.squareup.sqldelight:runtime-js:${Versions.sqlDelight}"
  const val jvmRuntime = "com.squareup.sqldelight:runtime-jvm:${Versions.sqlDelight}"
}
