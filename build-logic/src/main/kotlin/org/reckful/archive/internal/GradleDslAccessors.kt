package org.reckful.archive.internal

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * workaround for accessing version-catalog in convention plugins
 *
 * See https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
 */
internal val Project.libs : LibrariesForLibs
    get() = extensions.getByType()
