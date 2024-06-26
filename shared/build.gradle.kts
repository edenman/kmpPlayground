import com.android.build.api.dsl.AndroidSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

repositories(globalRepoList)

plugins {
  id("com.android.library")
  kotlin("multiplatform") // This uses the kotlin version from parent project.
  id("kotlin-parcelize")
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
  compileSdk = Versions.compileSdkVersion

  defaultConfig {
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
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
  namespace = "chat.quill.shareddata"
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
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = Versions.jvmTarget
    }
  }
  androidTarget {
    compilations.all {
      kotlinOptions.jvmTarget = Versions.jvmTarget
    }
  }

  sourceSets {
    all {
      languageSettings.optIn("io.ktor.util.KtorExperimentalAPI")
      languageSettings.optIn("kotlin.ExperimentalStdlibApi")
      languageSettings.optIn("kotlin.ExperimentalMultiplatform")
      languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      languageSettings.optIn("kotlinx.coroutines.ObsoleteCoroutinesApi")
      languageSettings.optIn(
        "kotlinx.serialization.ExperimentalSerializationApi"
      )
      languageSettings.optIn("kotlinx.serialization.InternalSerializationApi")
      languageSettings.optIn("kotlinx.serialization.UnsafeSerializationApi")
      languageSettings.optIn("kotlinx.coroutines.FlowPreview")
      languageSettings.optIn("kotlin.time.ExperimentalTime")
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
    val androidUnitTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(Kotlin.coroutinesTest)
      }
    }
  }
}


multiplatformSwiftPackage {
  swiftToolsVersion("5.3")
  targetPlatforms {
    iOS { v("13") }
  }
  packageName("QuillKMPSharedData")
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
    gradlew.writeText(
      "#!/bin/bash\n" +
          "export 'JAVA_HOME=${System.getProperty("java.home")}'\n" +
          "cd '${rootProject.rootDir}'\n" +
          "./gradlew \$@\n"
    )
    gradlew.setExecutable(true)
  }
}

fun AndroidSourceSet.repointToFolder(
  folderName: String
) {
  manifest.srcFile("$folderName/AndroidManifest.xml")
  java.srcDirs("$folderName/kotlin")
  res.srcDirs("$folderName/res")
}


// TODO is this still necessary?
// Workaround for https://youtrack.jetbrains.com/issue/KT-56019/Gradle-8-configuration-debugFrameworkIosFat-and-configuration-debugFrameworkIosArm64-contain-identical-attribute-sets
//val myAttribute = Attribute.of("com.coffeetrainlabs.kmpplayground.workaround", String::class.java)

//configurations.named("releaseFrameworkIosFat").configure {
//  attributes {
//    attribute(myAttribute, "release-all")
//  }
//}
//
//configurations.named("debugFrameworkIosFat").configure {
//  attributes {
//    attribute(myAttribute, "debug-all")
//  }
//}
