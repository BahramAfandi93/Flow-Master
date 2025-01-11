package com.calcpro.flowmaster.mapper

import com.calcpro.flowmaster.dao.entity.AppUser
import com.calcpro.flowmaster.dao.entity.Project
import com.calcpro.flowmaster.dto.ProjectRequest
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class ProjectMapper {
    fun projectToProjectRequest(project: Project) = ProjectRequest(
        projectName = project.projectName,
        projectLocation = project.projectLocation,
    )

    fun projectRequestToProject(projectRequest: ProjectRequest, user: AppUser) = Project(
        id = UUID.randomUUID().toString(),
        projectName = projectRequest.projectName,
        projectLocation = projectRequest.projectLocation,
        appUser = user
    )
}