pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version providers.gradleProperty("loom_version")
        id("com.github.gmazzo.buildconfig") version providers.gradleProperty("buildconfig_version")
        java
    }
}

rootProject.name = "oyvey-ported"