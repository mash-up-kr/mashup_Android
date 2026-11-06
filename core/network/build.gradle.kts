plugins {
    alias(libs.plugins.mashup.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mashup.network"
}

dependencies {
    implementation(project(":core:model"))

    // network
    api(libs.bundles.retrofit)
    api(libs.squareup.okhttp3.logging.interceptor)

    implementation(libs.bundles.moshi)
    ksp(libs.squareup.moshi.kotlin.codegen)
}
