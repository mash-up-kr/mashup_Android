plugins {
    alias(libs.plugins.mashup.android.library)
}

android {
    namespace = "com.mashup.core.common"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.viewpager2)

    // lifecycle
    api(libs.bundles.lifecycle)

    // material
    api(libs.google.android.material)

}
