package org.reckful.archive.conventions

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.reckful.archive.conventions.kotlin-jvm")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        // https://kotlinlang.org/docs/java-interop.html#jsr-305-support
        freeCompilerArgs += "-Xjsr305=strict"
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
