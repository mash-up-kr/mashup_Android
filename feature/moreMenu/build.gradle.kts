plugins {
    alias(libs.plugins.mashup.android.feature)
}

android {
    namespace = "com.mashup.feature.moreMenu"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":feature:moreMenu:notice"))

    implementation(libs.bundles.moshi)
    ksp(libs.squareup.moshi.kotlin.codegen)
}
