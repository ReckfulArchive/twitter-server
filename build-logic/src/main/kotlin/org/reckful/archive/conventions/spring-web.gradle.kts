package org.reckful.archive.conventions

import gradle.kotlin.dsl.accessors._1550a86bfc97b4e4cc3c4078f4decdaf.implementation
import org.reckful.archive.internal.libs

plugins {
    id("io.spring.dependency-management")
    id("org.reckful.archive.conventions.spring")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(libs.springdoc.openapi.starter)
}
