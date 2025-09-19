plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("projectBootstrap") {
            id = "com.jetbrains.morj.gradle.project-bootstrap"
            implementationClass = "com.jetbrains.morj.ProjectBootstrapPlugin"
        }
    }
}

group = "com.jetbrains.morj.gradle"
version = providers.gradleProperty("version").getOrElse("0.0.1-SNAPSHOT")

repositories {
    mavenCentral()
}

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
