plugins {
    alias(libs.plugins.mashup.android.library)
}

android {
    namespace = "com.mashup.core.testing"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

dependencies {
    api(libs.junit4)
    api(libs.turbine)
    api(libs.espresso)
    api(libs.composeJunitTest)
    api(libs.hiltTest)
    api(libs.mockito)
    api(libs.mockitoInline)

    debugApi(libs.composeManifestTest)
}
