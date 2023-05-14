package org.reckful.archive.twitter.vicinitas

import java.io.File

fun loadTestDataFile(fileName: String): File {
    return VicinitasTweet::class.java.classLoader.getResource(fileName)?.file?.let { File(it) }
        ?: error("Unable to load $fileName")
}
