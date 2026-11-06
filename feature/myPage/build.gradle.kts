plugins {
    alias(libs.plugins.mashup.android.feature)
}

android {
    namespace = "com.mashup.feature.mypage"

    defaultConfig {
        testInstrumentationRunner = "com.mashup.core.testing.MashUpTestRunner"
    }
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))

    implementation(libs.bundles.moshi)
    ksp(libs.squareup.moshi.kotlin.codegen)
}
