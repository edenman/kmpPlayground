import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories(globalRepoList)

plugins {
  id("com.android.library")
  kotlin("multiplatform") // This uses the kotlin version from parent project.
  id("kotlin-android-extensions")
  kotlin("plugin.serialization") version Versions.kotlin
  id("com.squareup.sqldelight")
  id("com.chromaticnoise.multiplatform-swiftpackage") version Versions.multiplatformSwiftPlugin
}

sqldelight {
  database("Database") {
    packageName = "chat.quill.data"
  }
}

android {
  compileSdkVersion(Versions.compileSdkVersion)

  androidExtensions {
    isExperimental = true
  }

  defaultConfig {
    minSdkVersion(Versions.minSdkVersion)
    targetSdkVersion(Versions.targetSdkVersion)
  }

  // Android Gradle Plugin expects sources to be in the "main" folder.
  // In order to keep our code structure consistent across platforms, we redefine
  // the sourceset directories here.
  sourceSets {
    getByName("main").repointToFolder("src/androidMain")
    getByName("test").repointToFolder("src/androidTest")
  }
  compileOptions {
    sourceCompatibility = Versions.javaVersion
    targetCompatibility = Versions.javaVersion
  }
  // Workaround for https://youtrack.jetbrains.com/issue/KT-43944#focus=Comments-27-4612683.0-0
  configurations {
    create("androidTestApi")
    create("androidTestDebugApi")
    create("androidTestReleaseApi")
    create("testApi")
    create("testDebugApi")
    create("testReleaseApi")
  }
}

kotlin {
  js {
    browser()
  }

  val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
      ::iosArm64
    else
      ::iosX64

  iOSTarget("ios") {
    binaries {
      framework {
        baseName = "Data"
      }
    }
  }

  android()

  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalMultiplatform")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ObsoleteCoroutinesApi")
      languageSettings.useExperimentalAnnotation(
        "kotlinx.serialization.ExperimentalSerializationApi"
      )
      languageSettings.useExperimentalAnnotation("kotlinx.serialization.InternalSerializationApi")
      languageSettings.useExperimentalAnnotation("kotlinx.serialization.UnsafeSerializationApi")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
      languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
    }
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation(kotlin("reflect"))
        implementation(Ktor.core)
        implementation(Ktor.websockets)
        implementation(Kotlin.coroutinesCore)
        api(Kotlin.serializationJson)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val iosMain by getting {
      dependencies {
        implementation(SqlDelight.nativeDriver)
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(SqlDelight.javascriptRuntime)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
        implementation(AndroidX.annotations)
        implementation(AndroidX.coreKtx)
        implementation(Square.phrase)
        implementation(SqlDelight.jvmRuntime)
        api(Ktor.clientOkhttp)
        api(Square.okhttp)
        implementation(Timber)
        api(AndroidX.emoji)
        implementation(UrlDetector)
      }
    }
    val androidTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(Kotlin.coroutinesTest)
      }
    }
  }
}

tasks.withType<Test> {
  testLogging {
    outputs.upToDateWhen { false }
    showStandardStreams = true
    events = setOf(FAILED, STANDARD_ERROR, STANDARD_OUT)
    showExceptions = true
    exceptionFormat = TestExceptionFormat.FULL
  }
}

// Couldn't figure out how to apply this on the android block directly so here it goes.
tasks.withType(KotlinCompile::class).all {
  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    languageVersion = Versions.kotlinLanguageVersion
  }
}

multiplatformSwiftPackage {
  swiftToolsVersion("5.3")
  targetPlatforms {
    iOS { v("13") }
  }
  packageName("QuillKMPSharedData")
}


fun AndroidSourceSet.repointToFolder(
  folderName: String
) {
  manifest.srcFile("$folderName/AndroidManifest.xml")
  java.srcDirs("$folderName/kotlin")
  res.srcDirs("$folderName/res")
}
