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
        if (project.hasProperty("gitHubPackages")) {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/morj/gradle-project-bootstrap")
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
            id = "com.jetbrains.morj.gradle.project-bootstrap"
            implementationClass = "com.jetbrains.morj.ProjectBootstrapPlugin"
        }
    }
}

repositories {
    mavenCentral()
}
