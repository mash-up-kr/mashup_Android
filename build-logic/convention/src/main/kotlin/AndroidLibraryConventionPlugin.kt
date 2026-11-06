import com.android.build.api.dsl.LibraryExtension
import com.mashup.convention.configureKotlinAndroid
import com.mashup.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("mashup.spotless")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                val targetSdkVersion = libs.versions.targetSdk.get().toInt()
                testOptions.targetSdk = targetSdkVersion
                lint.targetSdk = targetSdkVersion
            }
        }
    }
}
