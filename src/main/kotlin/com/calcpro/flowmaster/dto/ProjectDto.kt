package com.calcpro.flowmaster.dto

import com.calcpro.flowmaster.dao.entity.AppUser

data class ProjectRequest(
    val projectName: String,
    val projectLocation: String,
    val userId: AppUser? = null
)

class ProjectResponse(
    val projectName: String,
    val projectLocation: String
)