package com.calcpro.flowmaster.handlers

import com.calcpro.flowmaster.dao.entity.StructureShape.CIRCLE_CULVERT
import com.calcpro.flowmaster.dao.repository.StructureRepository
import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.dto.CulvertResponse
import com.calcpro.flowmaster.mapper.CulvertMapper
import com.calcpro.flowmaster.service.CulvertHandler
import com.calcpro.flowmaster.util.CalcUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CircleCulvertCalculator(
    private val structureRepository: StructureRepository,
    private val culvertMapper: CulvertMapper,
    private val calcUtil: CalcUtil
) : CulvertHandler {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun structureShape() = CIRCLE_CULVERT.type

    override fun culvertInit(culvertRequest: CulvertRequest): CulvertResponse {

        log.info("ActionLog.CircleCulvertCalculator.culvertInit: request received")

        var entity = culvertMapper.culvertRequestToCulvertEntity(culvertRequest)

        entity = calcUtil.circleCulvertCalculationSetter(entity)

        val responseEntity = structureRepository.save(entity)

        return culvertMapper.culvertEntityToCulvertResponse(responseEntity)
    }

    override fun addPipe(culvertRequest: CulvertRequest): CulvertResponse {
        TODO("Not yet implemented")
    }

}