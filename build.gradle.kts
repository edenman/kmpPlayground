// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories(globalRepoList)

  dependencies {
    // Leaving this one un-templated so the AS update mechanism is able to auto-update it.
    classpath("com.android.tools.build:gradle:4.2.0-alpha06")
    classpath(kotlin("gradle-plugin", version = Versions.kotlin))
  }
}

allprojects {
  repositories(globalRepoList)
}
