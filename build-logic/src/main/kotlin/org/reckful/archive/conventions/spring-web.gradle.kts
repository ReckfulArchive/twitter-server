package org.reckful.archive.conventions

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
    implementation(libs.springdoc.openapi.starter)
}
