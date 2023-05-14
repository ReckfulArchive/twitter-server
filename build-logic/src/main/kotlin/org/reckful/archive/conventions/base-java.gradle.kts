package org.reckful.archive.conventions

plugins {
    id("org.reckful.archive.conventions.base")
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
    withSourcesJar()
}


tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    testImplementation(kotlin("test"))
}
