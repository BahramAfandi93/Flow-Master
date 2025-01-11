package com.calcpro.flowmaster.controller

import com.calcpro.flowmaster.dto.ProjectRequest
import com.calcpro.flowmaster.service.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectController(
    private val projectService: ProjectService
) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(
        @AuthenticationPrincipal oauthUser: OAuth2User,  // Get active session user
        @RequestBody projectRequest: ProjectRequest
    ) = projectService.createProject(oauthUser, projectRequest)


    @GetMapping("/get-project/id/{id}")
    fun getProjectById(@PathVariable id: Long) = projectService.getProjectById(id)

}