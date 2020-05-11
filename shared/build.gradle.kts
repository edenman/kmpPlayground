import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  id("com.android.library")
  kotlin("multiplatform") // This uses the kotlin version from parent project.
  id("kotlin-android-extensions")
  kotlin("plugin.serialization") version "1.3.72"
  id("com.squareup.sqldelight")
}

repositories {
  google()
  mavenCentral()
  jcenter()
}

sqldelight {
  database("Database") {
    packageName = "com.coffeetrainlabs.kmpplayground"
  }
}

android {
  compileSdkVersion(29)

  androidExtensions {
    isExperimental = true
  }

  defaultConfig {
    minSdkVersion(28)
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

  val serializationVersion = "0.20.0"
  val ktorVersion = "1.3.2"
  val coroutinesVersion = "1.3.5"
  val timberVersion = "4.7.1"
  sourceSets {
    all {
      languageSettings.useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
      languageSettings.useExperimentalAnnotation("kotlin.ExperimentalMultiplatform")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ObsoleteCoroutinesApi")
      languageSettings.useExperimentalAnnotation("kotlinx.serialization.UnstableDefault")
      languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
    }
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-websockets:$ktorVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
        implementation(
          "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion"
        )
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serializationVersion")
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
        implementation("com.squareup.sqldelight:native-driver:1.3.0")
        implementation("io.ktor:ktor-client-ios:$ktorVersion")
        implementation("io.ktor:ktor-client-websockets-native:$ktorVersion")
        implementation(
          "org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationVersion"
        )
      }
    }
    val jsMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-js:$ktorVersion")
        implementation("io.ktor:ktor-client-websockets-js:$ktorVersion")
        implementation(
          "org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion"
        )
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
        implementation("androidx.core:core-ktx:1.2.0")
        implementation("androidx.annotation:annotation:1.1.0")
        implementation("com.squareup.phrase:phrase:1.1.0")
        implementation("com.squareup.sqldelight:runtime-jvm:1.3.0")
        api("io.ktor:ktor-client-okhttp:$ktorVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
        implementation("com.jakewharton.timber:timber:$timberVersion")
        api("androidx.emoji:emoji-bundled:1.1.0-beta01")
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
