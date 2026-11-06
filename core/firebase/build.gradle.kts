plugins {
    alias(libs.plugins.mashup.android.library)
    alias(libs.plugins.mashup.android.hilt)
}

android {
    namespace = "com.mashup.core.firebase"
}

dependencies {

    implementation(libs.kotlinx.coroutine.android)

    // google firebase
    api(platform(libs.google.firebase.bom))
    api(libs.bundles.firebase)

}
