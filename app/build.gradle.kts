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
    freeCompilerArgs = freeCompilerArgs +
        listOf(
          "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi"
        )
  }
  buildFeatures {
    viewBinding = true
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.androidxComposeCompiler
  }
    namespace = "com.coffeetrainlabs.kmpplayground"
}

kotlin {
  sourceSets.all {
    languageSettings.apply {
      languageVersion = Versions.kotlinLanguageVersion
      progressiveMode = true // non-exhaustive whens should be an error
    }
  }
}

dependencies {
  implementation(project(":shared"))

  implementation(AndroidX.coreKtx)
  implementation(AndroidX.appcompat)
  implementation(platform(AndroidX.composeBOM))
  implementation(AndroidX.composeAnimation)
  implementation(AndroidX.composeMaterial)
  implementation(AndroidX.composeUI)
  implementation(AndroidX.composeUITooling)
  implementation(AndroidX.composeFoundation)

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

  testImplementation(AndroidX.coreTesting)
  testImplementation(AndroidX.testCore)
  testImplementation(Google.truth)
  testImplementation(JUnit)
  testImplementation(Robolectric)
  testImplementation(AndroidX.emojiBundled)
}
