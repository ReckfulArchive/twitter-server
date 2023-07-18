package org.reckful.archive.twitter.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IndexController {
    @RequestMapping("/")
    fun indexPage(): String {
        return "redirect:/swagger-ui/index.html"
    }
}
