package com.calcpro.flowmaster.service

import com.calcpro.flowmaster.dao.entity.Project
import com.calcpro.flowmaster.dao.repository.ProjectRepository
import com.calcpro.flowmaster.dao.repository.UserRepository
import com.calcpro.flowmaster.dto.ErrorCode.USER_NOT_FOUND
import com.calcpro.flowmaster.dto.NotFoundException
import com.calcpro.flowmaster.dto.ProjectRequest
import com.calcpro.flowmaster.mapper.ProjectMapper
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val projectMapper: ProjectMapper
) {
    fun getProjectById(id: Long): Project = projectRepository.findById(id).get()

    fun createProject(oauthUser: OAuth2User, projectRequest: ProjectRequest) {

        val email = oauthUser.attributes["email"] as String  // Extract user email

        val user = userRepository.findByEmail(email)?: run {
            throw NotFoundException(USER_NOT_FOUND.code, USER_NOT_FOUND.message)
        }
        
        val projectEntity = projectMapper.projectRequestToProject(projectRequest, user)

        projectRepository.save(projectEntity)
    }
}