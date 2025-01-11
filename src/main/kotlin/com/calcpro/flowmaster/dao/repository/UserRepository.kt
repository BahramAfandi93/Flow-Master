package com.calcpro.flowmaster.dao.repository

import com.calcpro.flowmaster.dao.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<AppUser, String> {
    fun findByEmail(email: String): AppUser?
}