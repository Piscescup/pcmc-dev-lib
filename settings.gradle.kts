pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }

        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version providers
            .gradleProperty("loom_version")
            .get()
    }
}

rootProject.name = "pcmc-dev-lib"

include(
    "pcmc-dev-lib-api",
    "pcmc-dev-lib-impl",
    "pcmc-dev-lib-datagen"
)
