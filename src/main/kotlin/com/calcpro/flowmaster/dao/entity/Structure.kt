package com.calcpro.flowmaster.dao.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "structure")
class Structure(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    var chainage: String,
    var material: String,
    var flowHeight: Int,
    var rainIntensity: Int,
    var calculationArea: Double? = null,
    var slope: Double,

    @Enumerated(STRING)
    var shape: StructureShape,
    var structureDiameter: Double? = null,
    var structureWidth: Double? = null,
    var structureHeight: Double? = null,
    var centralAngle: Double? = null,
    var waterSpeed: Double? = null,
    var wettedPerimeter: Double? = null,
    var flowArea: Double? = null,
    var hydraulicRadius: Double? = null,
    var minAllowedSlope: Double? = null,
    var roughness: Double? = null,
    var flowRate: Double? = null,
    var requiredFlowRate: Double? = null,
    var result: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "project_id", nullable = false)
    val project: Project? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)