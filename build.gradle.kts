plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
}

group = "com.jetbrains.morj"
version = providers.gradleProperty("version").getOrElse("0.0.1-SNAPSHOT")

dependencies {
    implementation(gradleApi())
}

publishing {
    repositories {
        if (!project.hasProperty("ci")) {
            mavenLocal()
        }
    }
}

gradlePlugin {
    website = "https://github.com/morj/gradle-project-bootstrap"
    vcsUrl = "https://github.com/morj/gradle-project-bootstrap.git"
    plugins {
        create("projectBootstrap") {
            id = "com.jetbrains.morj.gradle.project-bootstrap"
            implementationClass = "com.jetbrains.morj.ProjectBootstrapPlugin"
            displayName = "Project Bootstrap Plugin"
            description = "A Gradle plugin for bootstrapping new projects with common configurations and structure"
            tags = listOf("bootstrap", "project-setup", "kotlin", "java")
        }
    }
}

repositories {
    mavenCentral()
}
