import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

val globalRepoList: RepositoryHandler.() -> Unit = {
  google()
  maven("https://plugins.gradle.org/m2/")
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

object Versions {
  val javaVersion = JavaVersion.VERSION_11
  const val jvmTarget = "1.8"
  const val kotlinLanguageVersion = "1.5"
  const val compileSdkVersion = 30
  const val minSdkVersion = 28
  const val targetSdkVersion = 29

  const val androidxAnnotations = "1.2.0-alpha01"
  const val androidxAppcompat = "1.3.0-alpha02"
  const val androidxCore = "1.5.0-alpha04"
  const val androidXEmoji = "1.2.0-alpha03"
  const val coil = "1.1.1"
  const val kotlin = "1.5.0"
  const val kotlinSerialization = "1.2.1"
  const val kotlinCoroutines = "1.5.0"
  const val ktor = "1.5.4"
  const val material = "1.3.0-alpha03"
  const val multiplatformSwiftPlugin = "2.0.3"
  const val okhttp = "4.10.0-RC1"
  const val phrase = "1.1.0"
  const val sqlDelight = "1.4.4"
  const val squareFlow = "1.0.0-alpha3"
  const val timber = "4.7.1"
  const val urlDetector = "0.1.23"
  const val xmlAPIs = "1.4.01"
}

const val Timber = "com.jakewharton.timber:timber:${Versions.timber}"
const val UrlDetector = "io.github.url-detector:url-detector:${Versions.urlDetector}"
const val XmlAPIs = "xml-apis:xml-apis:${Versions.xmlAPIs}"

object AndroidX {
  const val annotations = "androidx.annotation:annotation:${Versions.androidxAnnotations}"
  const val appcompat = "androidx.appcompat:appcompat:${Versions.androidxAppcompat}"
  const val coreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
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
