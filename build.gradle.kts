plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.room).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.googleGmsGoogleServices) apply false
    alias(libs.plugins.googleFirebaseCrashlytics) apply false
    alias(libs.plugins.kotlinCocoapods).apply(false)
}