package com.calcpro.flowmaster.dto

data class NotFoundException(val code: String, override val message: String? = null) : RuntimeException(message ?: code)