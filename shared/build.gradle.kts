import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

repositories(globalRepoList)

plugins {
  id("com.android.library")
  kotlin("multiplatform") // This uses the kotlin version from parent project.
  id("kotlin-android-extensions")
  kotlin("plugin.serialization") version Versions.kotlin
  id("com.squareup.sqldelight")
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
  }

  // Android Gradle Plugin expects sources to be in the "main" folder.
  // In order to keep our code structure consistent across platforms, we redefine
  // the sourceset directories here.
  sourceSets {
    repointSourceSetFolder("main", "src/androidMain")
    repointSourceSetFolder("test", "src/androidTest")
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
        api(Kotlin.serializationRuntime)
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

val packForXcode by tasks.creating(Sync::class) {
  val targetDir = File(buildDir, "xcode-frameworks")

  // selecting the right configuration for the iOS
  // framework depending on the environment
  // variables set by Xcode build
  val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
  val framework = kotlin.targets
    .getByName<KotlinNativeTarget>("ios")
    .binaries.getFramework(mode)
  inputs.property("mode", mode)
  dependsOn(framework.linkTask)

  from({ framework.outputDirectory })
  into(targetDir)

  // generate a helpful ./gradlew wrapper with embedded Java path
  doLast {
    val gradlew = File(targetDir, "gradlew")
    gradlew.writeText("#!/bin/bash\n" +
        "export 'JAVA_HOME=${System.getProperty("java.home")}'\n" +
        "cd '${rootProject.rootDir}'\n" +
        "./gradlew \$@\n")
    gradlew.setExecutable(true)
  }
}

@Suppress("DEPRECATION")
fun NamedDomainObjectContainer<AndroidSourceSet>.repointSourceSetFolder(
  sourceSetName: String,
  folderName: String
) {
  val test = getByName(sourceSetName)
  test.manifest.srcFile("$folderName/AndroidManifest.xml")
  test.java.srcDirs("$folderName/kotlin")
  test.res.srcDirs("$folderName/res")
}
