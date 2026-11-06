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
                add("implementation", project(":core:common"))
                add("implementation", project(":core:ui"))
                add("debugImplementation", project(":core:testing"))
            }
        }
    }
}
