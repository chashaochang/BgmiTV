buildscript {
    repositories {
        gradlePluginPortal()
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.android.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
