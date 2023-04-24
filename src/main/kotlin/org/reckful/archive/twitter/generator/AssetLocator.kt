package org.reckful.archive.twitter.generator

interface AssetLocator {
    fun locateImage(name: String): String
    fun locateCss(css: String): String
    fun locateJs(js: String): String
}
