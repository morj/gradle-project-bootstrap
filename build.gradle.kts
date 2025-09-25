plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
}

repositories {
    maven("https://cache-redirector.jetbrains.com/maven-central")
    maven("https://cache-redirector.jetbrains.com/plugins.gradle.org")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

group = "io.github.morj"
version = providers.gradleProperty("version").getOrElse("0.0.1-SNAPSHOT")

dependencies {
    implementation(gradleApi())
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.16.1")
    implementation("com.gradle:common-custom-user-data-gradle-plugin:1.12.1")
}

publishing {
    repositories {
        if (project.hasProperty("gitHubPackages")) {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/jetbrains/jcp-gradle-project-bootstrap")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        } else {
            mavenLocal()
        }
    }
}

gradlePlugin {
    plugins {
        create("projectBootstrap") {
            id = "io.github.morj.gradle.project-bootstrap"
            implementationClass = "io.github.morj.ProjectBootstrapPlugin"
        }
    }
}
