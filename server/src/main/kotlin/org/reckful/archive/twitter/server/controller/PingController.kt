package org.reckful.archive.twitter.server.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    @RequestMapping("/ping")
    fun ping(): String {
        return "OK"
    }
}
