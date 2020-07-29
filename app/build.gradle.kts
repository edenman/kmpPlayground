plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-android-extensions")
}

android {
  compileSdkVersion(29)

  defaultConfig {
    applicationId = "com.coffeetrainlabs.kmpplayground"
    minSdkVersion(28)
    targetSdkVersion(29)
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = freeCompilerArgs +
        listOf(
          "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi"
        )
  }
  buildFeatures {
    viewBinding = true
  }
  androidExtensions {
    isExperimental = true
    features = setOf("parcelize")
  }
  packagingOptions {
    exclude("META-INF/*.kotlin_module")
  }
}

dependencies {
  implementation(project(":shared"))

  implementation(AndroidX.coreKtx)
  implementation(AndroidX.appcompat)

  implementation(Google.materialUI)

  implementation(Kotlin.stdlib)
  implementation(Kotlin.stdlibJDK7)
  implementation(Kotlin.coroutinesCore)
  implementation(Kotlin.coroutinesAndroid)

  implementation(Square.flow)

  implementation(Coil.main)
  implementation(Coil.gif)
  implementation(Coil.svg)
  implementation(Coil.video)
}
