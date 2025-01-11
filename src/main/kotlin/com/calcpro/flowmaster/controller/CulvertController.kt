package com.calcpro.flowmaster.controller

import com.calcpro.flowmaster.dao.repository.ProjectRepository
import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.dto.CulvertResponse
import com.calcpro.flowmaster.service.CulvertService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/culvert")
class CulvertController(
    private val projectRepository: ProjectRepository,
    private val receiveCulvertService: CulvertService
) {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    @PostMapping("/save")
    fun createCulvert(
        @RequestParam projectId: Long,
        @RequestBody culvertRequest: CulvertRequest
    ): CulvertResponse? {

        log.debug(
            "ActionLog.CulvertController.createCulvert:  request sent to controller -> {}", culvertRequest
        )

        val project = projectRepository.findById(projectId).get()

        culvertRequest.projectId = projectId

        return receiveCulvertService.receiveCulvert(culvertRequest)
    }
}