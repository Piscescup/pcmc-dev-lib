dependencies {
    implementation(project(":pcmc-dev-lib-api"))
    implementation(project(":pcmc-dev-lib-impl"))

    testImplementation(
        platform("org.junit:junit-bom:6.0.0")
    )
    testImplementation(
        "org.junit.jupiter:junit-jupiter"
    )
    testRuntimeOnly(
        "org.junit.platform:junit-platform-launcher"
    )
}

tasks.test {
    useJUnitPlatform()
}