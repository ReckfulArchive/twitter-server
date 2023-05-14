plugins {
    id("org.reckful.archive.conventions.kotlin-jvm")
    id("org.reckful.archive.conventions.spring-web")
}

dependencies {
    implementation(projects.parsers.vicinitas)
}
