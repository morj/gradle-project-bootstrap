package com.jetbrains.morj

import org.gradle.api.initialization.Settings
import java.net.URI
import java.util.Properties

/**
 * Configures a Space Maven repository with authentication in pluginManagement repositories
 *
 * @param proj The Space project name
 * @param repo The repository name within the project
 */
fun Settings.spaceRepository(proj: String, repo: String) {
    pluginManagement.repositories.maven {
        name = "$proj-$repo"
        url = URI("https://packages.jetbrains.team/maven/p/$proj/$repo")
        credentials {
            username = resolveProperty("spaceUsername", "SPACE_USERNAME")
            password = resolveProperty("spacePassword", "SPACE_PASSWORD")
        }
    }
}

/**
 * Resolves a property from multiple sources in order of priority:
 * 1. Gradle property
 * 2. Local properties file (gradle.local.properties)
 * 3. System property
 * 4. Environment variable (with provided name)
 * 5. Environment variable (with alternative name if provided)
 *
 * @param name The property name to resolve
 * @param alternativeEnvName Alternative environment variable name to check
 * @return The resolved property value
 * @throws IllegalStateException if the property cannot be resolved
 */
fun Settings.resolveProperty(name: String, alternativeEnvName: String? = null): String =
    resolvePropertyOrNull(name, alternativeEnvName)
        ?: error("Failed to resolve gradle property $name")

/**
 * Resolves a property from multiple sources in order of priority, returning null if not found
 * 1. Gradle property
 * 2. Local properties file (gradle.local.properties)
 * 3. System property
 * 4. Environment variable (with provided name)
 * 5. Environment variable (with alternative name if provided)
 *
 * @param name The property name to resolve
 * @param alternativeEnvName Alternative environment variable name to check
 * @return The resolved property value or null if not found
 */
fun Settings.resolvePropertyOrNull(name: String, alternativeEnvName: String? = null): String? =
    providers.gradleProperty(name).orNull
        ?: localProperties().getProperty(name)
        ?: System.getProperty(name)
        ?: System.getenv(name)
        ?: alternativeEnvName?.let { System.getenv(alternativeEnvName) }

private var localProperties: Properties? = null

/**
 * Loads and caches the local properties from gradle.local.properties file
 *
 * @return Properties object containing the local properties
 */
fun Settings.localProperties(): Properties {
    if (localProperties == null) {
        localProperties = Properties().also { props ->
            rootDir.resolve("gradle.local.properties").takeIf { it.exists() }?.let {
                it.inputStream().use { props.apply { load(it) } }
            }
        }
    }
    return localProperties!!
}
