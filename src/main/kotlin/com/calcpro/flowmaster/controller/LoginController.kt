package com.calcpro.flowmaster.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun showLoginPage(): String {
        return "login"  // This must match src/main/resources/templates/login.html
    }
}