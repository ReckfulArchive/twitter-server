package org.reckful.archive.twitter.server.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Hidden
@Controller
class IndexController {
    @RequestMapping("/")
    fun indexPage(): String {
        return "redirect:/swagger-ui/index.html"
    }
}
