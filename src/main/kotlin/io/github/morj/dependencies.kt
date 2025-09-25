@file:Suppress("unused")

package io.github.morj

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.DependencyResolutionManagement
import org.gradle.api.model.ObjectFactory
import java.net.URI

val Settings.jcpCommonBOM
    get() = "com.jetbrains.jcp:jcp-common-bom:$jcpCommonVersion"

val Settings.jcpCommonVersion
    get() = resolveProperty("jcpCommonVersion", "JCP_COMMON_VERSION")

val Project.jcpCommonBOM
    get() = "com.jetbrains.jcp:jcp-common-bom:$jcpCommonVersion"

val Project.jcpCommonVersion
    get() = resolveProperty("jcpCommonVersion", "JCP_COMMON_VERSION")

fun Settings.jcpDependencyResolutionManagement(
    objectFactory: ObjectFactory,
    dependencyResolutionConfig: DependencyResolutionManagement.() -> Unit = {}
) {
    // TODO:
    // val extension = settings.extensions.create("catalogConfig", VersionCatalogSettingsExtension::class.java)

    val projectRoot = layout.rootDirectory
    dependencyResolutionManagement {
        repositories {
            maven {
                url = URI("https://packages.jetbrains.team/maven/p/jcp/github-mirror")
                credentials {
                    username = resolveProperty("spaceUsername", "SPACE_USERNAME")
                    password = resolveProperty("spacePassword", "SPACE_PASSWORD")
                }
            }
        }
        versionCatalogs {
            val platformVersionCatalog = projectRoot.file("gradle/platform-catalog.versions.toml")
            if (platformVersionCatalog.asFile.exists()) {
                create("libs") {
                    from(objectFactory.fileCollection().from(platformVersionCatalog))
                }
            } else {
                create("jcp") {
                    val version = resolveProperty("jcpCommonVersion", "JCP_COMMON_VERSION")
                    from("com.jetbrains.jcp:jcp-common-versions:$version")
                }
            }
        }
        dependencyResolutionConfig()
    }
    settings.pluginManager.apply("com.gradle.enterprise")
    settings.pluginManager.apply("com.gradle.common-custom-user-data-gradle-plugin")
}
