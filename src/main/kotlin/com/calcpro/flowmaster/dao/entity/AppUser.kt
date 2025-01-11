package com.calcpro.flowmaster.dao.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "app_user")
class AppUser(
    @Id
    var id: String? = null,
    var name: String?,
    var lastname: String? = null,
    var email: String? = null,
    var password: String? = null,
    var birthday: LocalDate? = null,
    var position: String? = null,
    var company: String? = null,
    var phone: String? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
)