package com.jetbrains.morj

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class ProjectBootstrapPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        //val layout = settings.layout
        //settings.dependencyResolutionManagement {
        //    versionCatalogs {
        //        create("libs") {
        //            from(layout.settingsDirectory.file("gradle/libs.versions.toml"))
        //        }
        //    }
        //}
    }
}
