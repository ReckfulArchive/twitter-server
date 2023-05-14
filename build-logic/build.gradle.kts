plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

dependencies {
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.spring.kotlin)
    implementation(libs.gradlePlugin.spring.boot)
    implementation(libs.gradlePlugin.spring.dependencyManagement)

    // workaround for accessing version-catalog in convention plugins
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
