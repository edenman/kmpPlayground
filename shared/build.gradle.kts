import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  id("com.android.library")
  kotlin("multiplatform") // This uses the kotlin version from parent project.
  id("kotlin-android-extensions")
  kotlin("plugin.serialization") version Versions.kotlin
}

repositories {
  google()
  mavenCentral()
  jcenter()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
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
    val main = getByName("main")
    main.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    main.java.srcDirs("src/androidMain/kotlin")
    main.res.srcDirs("src/androidMain/res")

    val test = getByName("test")
    test.manifest.srcFile("src/androidTest/AndroidManifest.xml")
    test.java.srcDirs("src/androidTest/kotlin")
    test.res.srcDirs("src/androidTest/res")
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
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      languageSettings.useExperimentalAnnotation("kotlinx.serialization.UnsafeSerializationApi")
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
