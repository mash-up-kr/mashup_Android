import com.mashup.convention.debugImplementation
import com.mashup.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("mashup.android.library")
                apply("mashup.android.library.compose")
                apply("mashup.android.hilt")
            }

            dependencies {
                implementation(project(":core:common"))
                implementation(project(":core:ui"))
                debugImplementation(project(":core:testing"))
            }
        }
    }
}
