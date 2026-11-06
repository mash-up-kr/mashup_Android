plugins {
    alias(libs.plugins.mashup.android.feature)
}

android {
    namespace = "com.mashup.feature.danggn"

    defaultConfig {
        testInstrumentationRunner = "com.mashup.core.testing.MashUpTestRunner"
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":feature:myPage"))

    implementation(libs.google.accompanist.swiperefresh)
    implementation(libs.konfetti.compose)

    implementation(libs.bundles.moshi)
    ksp(libs.squareup.moshi.kotlin.codegen)
}
