plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-parcelize")
}

android {
  compileSdk = Versions.compileSdkVersion

  defaultConfig {
    applicationId = "com.coffeetrainlabs.kmpplayground"
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
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
    sourceCompatibility = Versions.javaVersion
    targetCompatibility = Versions.javaVersion
  }
  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    languageVersion = Versions.kotlinLanguageVersion
    freeCompilerArgs = freeCompilerArgs +
        listOf(
          "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi"
        )
  }
  buildFeatures {
    viewBinding = true
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

  implementation(Timber)
}
