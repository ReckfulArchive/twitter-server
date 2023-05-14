package org.reckful.archive.conventions

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.reckful.archive.conventions.base")
    id("org.reckful.archive.conventions.base-java")
    kotlin("jvm")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        allWarningsAsErrors.set(true)
    }
}

