package com.calcpro.flowmaster.service

import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.dto.CulvertResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CulvertService(
    private val culvertHandlerFactory: CulvertHandlerFactory,
) {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    fun receiveCulvert(culvertRequest: CulvertRequest): CulvertResponse? {

        val handler = culvertHandlerFactory.getHandler(culvertRequest)

        log.debug("ActionLog.CulvertService.receiveCulvert: handler chosen -> {}", handler)

        val response = handler?.culvertInit(culvertRequest)

        log.debug("ActionLog.CulvertService.receiveCulvert: culvert response received -> {}", response)

        return response
    }

}