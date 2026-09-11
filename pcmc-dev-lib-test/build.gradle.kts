plugins {
    id("java")
}

group = "io.github.piscescup"
version = "1.0.0"

repositories {
    mavenCentral()
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":pcmc-dev-lib-api"))
    implementation(project(":pcmc-dev-lib-impl"))
    implementation(project(":pcmc-dev-lib-datagen"))
}

tasks.test {
    useJUnitPlatform()
}