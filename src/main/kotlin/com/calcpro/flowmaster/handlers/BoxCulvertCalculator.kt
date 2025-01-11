package com.calcpro.flowmaster.handlers

import com.calcpro.flowmaster.dao.entity.StructureShape.BOX_CULVERT
import com.calcpro.flowmaster.dao.repository.StructureRepository
import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.dto.CulvertResponse
import com.calcpro.flowmaster.mapper.CulvertMapper
import com.calcpro.flowmaster.service.CulvertHandler
import com.calcpro.flowmaster.util.CalcUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class BoxCulvertCalculator(
    private val structureRepository: StructureRepository,
    private val culvertMapper: CulvertMapper,
    private val calcUtil: CalcUtil
) : CulvertHandler {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun structureShape() = BOX_CULVERT.type

    override fun culvertInit(culvertRequest: CulvertRequest): CulvertResponse {

        log.info("ActionLog.BoxCulvertCalculator.culvertInit: request received")

        var entity = culvertMapper.culvertRequestToCulvertEntity(culvertRequest)
        entity = calcUtil.boxCulvertCalculationSetter(entity)

        val responseEntity = structureRepository.save(entity)

        return culvertMapper.culvertEntityToCulvertResponse(responseEntity)
    }

    override fun addPipe(culvertRequest: CulvertRequest): CulvertResponse {
        TODO("Not yet implemented")
    }
}