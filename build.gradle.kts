import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
	id("net.fabricmc.fabric-loom") apply false
}

val minecraftVersion = providers.gradleProperty("minecraft_version").get()
val loaderVersion = providers.gradleProperty("loader_version").get()
val fabricApiVersion = providers.gradleProperty("fabric_api_version").get()
val libraryVersion = providers.gradleProperty("version").get()
val mavenGroup = providers.gradleProperty("group").get()
val commonsLibVersion = providers.gradleProperty("commons_lib_version").get()

allprojects {
	group = mavenGroup
	version = libraryVersion

	repositories {
		mavenCentral()

		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
	}
}

subprojects {
	apply(plugin = "net.fabricmc.fabric-loom")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")

	dependencies {
		add("minecraft", "com.mojang:minecraft:$minecraftVersion")
		add("implementation", "net.fabricmc:fabric-loader:$loaderVersion")
		add(
			"implementation",
			"net.fabricmc.fabric-api:fabric-api:$fabricApiVersion"
		)
		add(
			"implementation",
			"io.github.piscescup:commons-lib:$commonsLibVersion"
		)
	}

	extensions.configure<JavaPluginExtension> {
		toolchain {
			languageVersion = JavaLanguageVersion.of(25)
		}

		sourceCompatibility = JavaVersion.VERSION_25
		targetCompatibility = JavaVersion.VERSION_25

		withSourcesJar()
		withJavadocJar()
	}

	tasks.withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
		options.release = 25
	}

	tasks.named<ProcessResources>("processResources") {
		val moduleVersion = project.version.toString()

		inputs.property("version", moduleVersion)

		filesMatching("fabric.mod.json") {
			expand("version" to moduleVersion)
		}
	}

	tasks.named<Jar>("jar") {
		val moduleName = project.name
		inputs.property("moduleName", moduleName)

		from(rootProject.file("LICENSE")) {
			rename { "${it}_$moduleName" }
		}
	}

	extensions.configure<PublishingExtension> {
		publications {
			register<MavenPublication>("mavenJava") {
				from(components["java"])
				artifactId = project.name
			}
		}
	}
}

project(":pcmc-dev-lib-impl") {
	dependencies {
		add(
			"api",
			project(":pcmc-dev-lib-api")
		)
	}
}

project(":pcmc-dev-lib-datagen") {
	dependencies {
		add(
			"implementation",
			project(":pcmc-dev-lib-impl")
		)
	}
}

tasks.register("buildAll") {
	group = "build"
	description = "Builds all PCMC Dev Lib modules."

	dependsOn(subprojects.map { "${it.path}:build" })
}

tasks.register("publishAllToMavenLocal") {
	group = "publishing"
	description = "Publishes all PCMC Dev Lib modules to Maven Local."

	dependsOn(subprojects.map { "${it.path}:publishToMavenLocal" })
}
