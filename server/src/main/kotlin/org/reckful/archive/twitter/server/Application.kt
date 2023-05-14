package org.reckful.archive.twitter.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["org.reckful.archive.twitter"])
@SpringBootApplication(scanBasePackages = ["org.reckful.archive.twitter"])
class TwitterDataApplication

fun main(args: Array<String>) {
    runApplication<TwitterDataApplication>(*args)
}
