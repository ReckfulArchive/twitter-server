@file:Suppress("UnstableApiUsage")

rootProject.name = "twitter-server"

pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    ":parsers",
    ":parsers:vicinitas",
    ":server",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
