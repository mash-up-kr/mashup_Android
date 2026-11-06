plugins {
    alias(libs.plugins.mashup.android.feature)
}

android {
    namespace = "com.mashup.feature.setting"

    defaultConfig {
        testInstrumentationRunner = "com.mashup.core.testing.MashUpTestRunner"
    }
}

dependencies {
    implementation(project(":core:datastore"))
}
