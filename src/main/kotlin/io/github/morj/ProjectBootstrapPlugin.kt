package io.github.morj

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

class ProjectBootstrapPlugin @Inject constructor(private val objectFactory: ObjectFactory) : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.jcpDependencyResolutionManagement(objectFactory)
        settings.enableGE("ge.labs.jb.gg", settings.resolveProperty("defaultDevelocityKey", "DEFAULT_DEVELOCITY_KEY"))
    }
}
