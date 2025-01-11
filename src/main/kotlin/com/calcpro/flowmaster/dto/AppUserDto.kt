package com.calcpro.flowmaster.dto

import java.time.LocalDate

data class UserRequestDto(
    val name: String?,
    val lastname: String?,
    val email: String?,
    val password: String?,
    val birthday: LocalDate?,
    val position: String? = null,
    val company: String? = null,
    val phone: String? = null
)

data class UserResponse(
    val name: String?,
    val lastname: String?,
    val email: String?,
    val password: String?,
    val birthday: LocalDate?,
    val position: String? = null,
    val company: String? = null,
    val phone: String? = null
)