import com.mashup.convention.implementation
import com.mashup.convention.ksp
import com.mashup.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                implementation(libs.google.dagger.hilt.android)
                ksp(libs.google.dagger.hilt.compiler)
            }
        }
    }
}
