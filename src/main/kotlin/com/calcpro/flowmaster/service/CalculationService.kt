package com.calcpro.flowmaster.service

import com.calcpro.flowmaster.dao.entity.Result.FLOW_FAILED
import com.calcpro.flowmaster.dao.entity.Result.FLOW_IS_SATISFIED
import com.calcpro.flowmaster.dao.entity.StructureEntity
import com.calcpro.flowmaster.dao.entity.StructureShape.BOX_CULVERT
import com.calcpro.flowmaster.dao.entity.StructureShape.CIRCLE_CULVERT
import com.calcpro.flowmaster.dto.CulvertRequest
import com.calcpro.flowmaster.mapper.CulvertMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import org.springframework.stereotype.Service

@Service
class CalculationService(
    private val culvertMapper: CulvertMapper,
) {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    fun calculateCulvert(request: CulvertRequest): StructureEntity {
        log.info { "ActionLog.calculateCulvert.start: request = $request" }
        val entity = culvertMapper.culvertRequestToCulvertEntity(request)
        val percent = entity.flowHeight
        val slope = entity.slope

        val structure = when (request.shape) {
            CIRCLE_CULVERT -> {
                val diameter = entity.structureDiameter!!

                entity.centralAngle = getCircleCulvertCentralAngle(diameter, percent)
                entity.flowArea = getCircleFlowArea(diameter, percent)
                entity.wettedPerimeter = getCircleWettedPerimeter(diameter, percent)
                entity.waterSpeed = getCircleWaterSpeed(slope, diameter, percent)
                entity.hydraulicRadius = getCircleHydraulicRadius(diameter, percent)
                entity
            }

            BOX_CULVERT -> {
                val width = entity.structureWidth!!
                val height = entity.structureHeight!!
                val flowHeight = entity.flowHeight

                entity.flowArea = getBoxFlowArea(width, height, flowHeight)
                entity.wettedPerimeter = getBoxWettedPerimeter(width, height)
                entity.waterSpeed = getBoxWaterSpeed(width, height, flowHeight, slope)
                entity.hydraulicRadius = getBoxHydraulicRadius(width, height, flowHeight)
                entity
            }

            else -> TODO("Not yet implemented")
        }

        entity.roughness = 0.014
        entity.flowRate = getFlowRate(entity)
        entity.requiredFlowRate = getRequiredFlowRate(entity)
        entity.result = getResult(entity.flowRate, entity.requiredFlowRate)

        log.info { "ActionLog.calculateCulvert.success" }
        return structure
    }

    private fun getCircleCulvertCentralAngle(diameter: Double, percent: Int): Double {
        val radius = (diameter / 2)

        val height = if (percent < 50) {
            diameter * percent * 0.01
        } else {
            diameter - diameter * percent * 0.01
        }

        val centralAngle = 2 * acos((radius - height) / radius)
        return "%.2f".format(centralAngle).toDouble()
    }

    private fun getCircleFlowArea(diameter: Double, percent: Int): Double {
        val centralAngle = getCircleCulvertCentralAngle(diameter, percent)
        val circularSegmentArea: Double = ((diameter / 2).pow(2.0) * (centralAngle - sin(centralAngle))) / 2

        val flowArea = if (percent < 50) {
            circularSegmentArea
        } else {
            Math.PI * (diameter / 2).pow(2.0) - circularSegmentArea
        }
        return "%.2f".format(flowArea).toDouble()
    }

    private fun getBoxFlowArea(width: Double, height: Double, flowHeight: Int): Double {
        val boxFlowArea = "%.2f".format(width * height * flowHeight / 100).toDouble()
        return "%.2f".format(boxFlowArea).toDouble()
    }

    private fun getCircleWettedPerimeter(diameter: Double, percent: Int): Double {
        val centralAngle = getCircleCulvertCentralAngle(diameter, percent)

        val wettedPerimeter = if (percent < 50) {
            (diameter / 2) * centralAngle
        } else {
            2 * Math.PI * (diameter / 2) - (diameter / 2) * centralAngle
        }

        return "%.2f".format(wettedPerimeter).toDouble()
    }

    private fun getBoxWettedPerimeter(width: Double, height: Double): Double {
        val boxWettedPerimeter = 2 * height + width
        return "%.2f".format(boxWettedPerimeter).toDouble()
    }

    private fun getCircleHydraulicRadius(diameter: Double, percent: Int): Double {
        val circleCulvertHydraulicRadius =
            getCircleFlowArea(diameter, percent) / getCircleWettedPerimeter(diameter, percent)
        return "%.2f".format(circleCulvertHydraulicRadius).toDouble()
    }

    private fun getBoxHydraulicRadius(width: Double, height: Double, flowHeight: Int): Double {
        val boxHydraulicRadius = getBoxFlowArea(width, height, flowHeight) / getBoxWettedPerimeter(width, height)
        return "%.2f".format(boxHydraulicRadius).toDouble()
    }

    private fun getCircleWaterSpeed(slope: Double, diameter: Double, percent: Int): Double {
        val rad = getCircleHydraulicRadius(diameter, percent)
//        TODO(val valueN = material.let { Roughness.valueOf(it) })
        val powValue = 0.666 - 0.014 * sqrt(rad) //0.014 - the roughness should be calculated in future
        val waterSpeed = 71.4 * rad.pow(powValue) * sqrt(slope / 100)
        return "%.2f".format(waterSpeed).toDouble()
    }

    private fun getBoxWaterSpeed(width: Double, height: Double, flowHeight: Int, slope: Double): Double {
        val rad = getBoxHydraulicRadius(width, height, flowHeight)
//        TODO(val valueN = material.let { Roughness.valueOf(it) })
        val powValue = 0.666 - 0.014 * sqrt(rad) //0.014 - the roughness should be calculated in future
        val waterSpeed = 71.4 * rad.pow(powValue) * sqrt(slope / 100)
        return "%.2f".format(waterSpeed).toDouble()
    }

    private fun getFlowRate(entity: StructureEntity): Double {
        val flowArea = entity.flowArea!!
        val waterSpeed = entity.waterSpeed!!
        val flowRate = flowArea * waterSpeed * 1000
        return "%.2f".format(flowRate).toDouble()
    }

    private fun getRequiredFlowRate(entity: StructureEntity): Double {
        val rainIntensity = entity.rainIntensity
        val calculationArea = entity.calculationArea
        val requiredFlowRate = rainIntensity * calculationArea!!
        return "%.2f".format(requiredFlowRate).toDouble()
    }

    private fun getResult(flowRate: Double?, requiredFlowRate: Double?): String {

        return if (flowRate == null || requiredFlowRate == null) {
            return FLOW_FAILED.toString()
        } else if (flowRate > requiredFlowRate) {
            FLOW_IS_SATISFIED.toString()
        } else {
            FLOW_FAILED.toString()
        }
    }
}