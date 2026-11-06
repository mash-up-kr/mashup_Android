plugins {
    alias(libs.plugins.mashup.android.library)
    alias(libs.plugins.mashup.android.hilt)
}

android {
    namespace = "com.mashup.core.data"
}

dependencies {
    implementation(project(":core:network"))
}
