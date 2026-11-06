// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.mashup.spotless) apply false
}

// TODO: Task 6에서 삭제 (모듈이 mashup.android.* 플러그인으로 spotless를 적용하게 되면 불필요)
subprojects {
    apply(plugin = "mashup.spotless")
}
