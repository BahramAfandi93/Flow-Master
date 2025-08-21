package com.calcpro.flowmaster.controller

import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.service.CalculationService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// this controller is for MVP and will be used to calculate the structure for MVP stage
// will be replaced in feature

@CrossOrigin
@RestController
@RequestMapping("/simple/calculate")
class CalculationController(
    private val calculationService: CalculationService
) {
    @PostMapping("/culvert")
    fun calculateCircleStructure(
        @RequestBody culvertRequest: CulvertRequest
    ) = calculationService.calculateCulvert(culvertRequest)
}