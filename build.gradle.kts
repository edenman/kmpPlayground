import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT

buildscript {
  repositories(globalRepoList)

  dependencies {
    classpath(XmlAPIs) // Workaround for https://github.com/cashapp/sqldelight/issues/1343
    // Leaving this one un-templated so the AS update mechanism is able to auto-update it.
    classpath("com.android.tools.build:gradle:7.1.0-rc01")
    classpath(kotlin("gradle-plugin", version = Versions.kotlin))
    classpath(SqlDelight.gradlePlugin)
  }
}

allprojects {
  repositories(globalRepoList)
  tasks.withType<Test> {
    testLogging {
      outputs.upToDateWhen { false }
      showStandardStreams = true
      events = setOf(FAILED, STANDARD_ERROR, STANDARD_OUT)
      showExceptions = true
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

      // Attempt to prevent memory thrash causing builds to hang on CI.
      // From https://github.com/robolectric/robolectric/issues/5131#issuecomment-509631890
      maxParallelForks = 2
      setForkEvery(80)
      maxHeapSize = "2048m"
      minHeapSize = "1024m"
    }
  }
}
