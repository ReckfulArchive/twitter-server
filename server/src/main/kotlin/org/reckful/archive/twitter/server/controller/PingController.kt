package org.reckful.archive.twitter.server.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class PingController {

    @GetMapping("/ping")
    fun ping(): String {
        return "OK"
    }
}
