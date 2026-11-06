plugins {
    alias(libs.plugins.mashup.android.feature)
}

android {
    namespace = "com.mashup.feature.moreMenu.notice"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
