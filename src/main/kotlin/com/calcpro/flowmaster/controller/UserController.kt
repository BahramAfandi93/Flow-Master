package com.calcpro.flowmaster.controller

import com.calcpro.flowmaster.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/")
    fun home(): String {
        return "Hello, Home!"
    }

    @GetMapping("/secured")
    fun secured(): String {
        return "Hello, Secured!"
    }
}