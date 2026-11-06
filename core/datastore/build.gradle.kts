plugins {
    alias(libs.plugins.mashup.android.library)
    alias(libs.plugins.mashup.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mashup.datastore"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.datastore)
    implementation(libs.kotlin.serialization)
}
