import java.util.Properties

plugins {
    alias(libs.plugins.mashup.android.application)
    alias(libs.plugins.mashup.android.application.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

val properties = Properties().apply {
    rootProject.file("local.properties").inputStream().use { load(it) }
}
val naverClientId = properties.getProperty("naver.client.id").toString()

android {
    namespace = "com.mashup"

    lint {
        disable += setOf(
            "JvmStaticProvidesInObjectDetector",
            "FieldSiteTargetOnQualifierAnnotation",
            "ModuleCompanionObjects",
            "ModuleCompanionObjectsNotInModuleParent",
        )
    }

    defaultConfig {
        applicationId = "com.mashup"
        versionCode = 38
        versionName = "1.7.3"

        testInstrumentationRunner = "com.mashup.core.testing.MashUpTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "API_KEY", "\"${properties["api.key"]}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${properties["naver.client.id"]}\"")
    }

    sourceSets {
        getByName("release") {
            java.setSrcDirs(listOf("src/main/java", "src/release/java"))
        }
        getByName("debug") {
            java.setSrcDirs(listOf("src/main/java", "src/debug/java"))
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["appName"] = "@string/app_name_debug"
            manifestPlaceholders["naver_client_id"] = naverClientId
            isMinifyEnabled = false
            buildConfigField("Boolean", "DEBUG_MODE", "true")
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isDebuggable = false
            manifestPlaceholders["appName"] = "@string/app_name"
            manifestPlaceholders["naver_client_id"] = naverClientId
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        )
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:firebase"))
    implementation(project(":core:data"))
    implementation(project(":feature:setting"))
    implementation(project(":feature:danggn"))
    implementation(project(":feature:myPage"))
    implementation(project(":feature:moreMenu"))
    implementation(project(":feature:moreMenu:notice"))

    // ml Kit
    implementation(libs.google.mlkit)

    // camera
    implementation(libs.bundles.camera)

    // hilt
    implementation(libs.google.dagger.hilt.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    kapt(libs.google.dagger.hilt.compiler)

    // network
    implementation(libs.bundles.retrofit)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3.logging.interceptor)
    // image
    implementation(libs.github.bumptech.glide)
    kapt(libs.github.bumptech.glide.compiler)

    // DataStore
    implementation(libs.androidx.datastore)

    // serialization
    implementation(libs.bundles.moshi)
    kapt(libs.squareup.moshi.kotlin.codegen)

    // Navigation
    implementation(libs.bundles.navigation)

    // compose webview
    implementation(libs.google.accompanist.webview)
    implementation(libs.androidx.compose.ui.viewBinding)
    implementation(libs.androidx.constraintlayout.compose)


    // lifecycle viewmodel compose
    implementation(libs.bundles.lifecycle)

    // coroutine test
    testImplementation(libs.kotlinx.coroutine.test)

    // Naver Map
    implementation(libs.github.fornewid)

    // Date Time
    implementation(libs.jakewharton.threetenabp)
}

