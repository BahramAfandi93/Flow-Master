package com.calcpro.flowmaster.dto

enum class ErrorCode(val code: String, val message: String) {
    USER_NOT_FOUND("USER.NOT_FOUND", "User not found"),
    DEPOSIT_FILE_NOT_FOUND("DEPOSIT.FILE.NOT_FOUND", "Deposit file not found"),
}