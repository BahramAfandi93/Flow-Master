package com.calcpro.flowmaster.util

import com.calcpro.flowmaster.dao.entity.Result.FLOW_FAILED
import com.calcpro.flowmaster.dao.entity.Result.FLOW_IS_SATISFIED
import com.calcpro.flowmaster.dao.entity.Structure
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import org.springframework.stereotype.Component

@Component
class CalcUtil {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    fun circleCulvertCalculationSetter(entity: Structure): Structure {
        log.info { "ActionLog.CalcUtil.circleCulvertCalculationSetter: Entity received -> ${entity}" }
        entity.centralAngle = getCircleCulvertCentralAngle(entity)
        entity.flowArea = getCircleFlowArea(entity)
        entity.wettedPerimeter = getCircleWettedPerimeter(entity)
        entity.waterSpeed = getCircleWaterSpeed(entity)
        entity.hydraulicRadius = getCircleHydraulicRadius(entity)
        entity.roughness = 0.014
        entity.flowRate = getFlowRate(entity)
        entity.requiredFlowRate = getRequiredFlowRate(entity)
        entity.result = getResult(entity)

        return entity
    }

    fun boxCulvertCalculationSetter(entity: Structure): Structure {
        log.info { "ActionLog.CalcUtil.boxCulvertCalculationSetter: Entity received -> ${entity}" }
        entity.flowArea = getBoxFlowArea(entity)
        entity.wettedPerimeter = getBoxWettedPerimeter(entity)
        entity.waterSpeed = getBoxWaterSpeed(entity)
        entity.hydraulicRadius = getBoxHydraulicRadius(entity)
        entity.roughness = 0.014
        entity.flowRate = getFlowRate(entity)
        entity.requiredFlowRate = getRequiredFlowRate(entity)
        entity.result = getResult(entity)

        return entity
    }

    private fun getCircleCulvertCentralAngle(entity: Structure): Double {
        val diameter = entity.structureDiameter!!
        val percent = entity.flowHeight
        log.info { "ActionLog.CalcUtil.getCentralAngle -> diameter = $diameter, percent = $percent" }

        val radius = diameter / 2
        val height = if (percent < 50) {
            diameter * percent * 0.01
        } else {
            diameter - diameter * percent * 0.01
        }

        val centralAngle = 2 * acos((radius - height) / radius)

        return "%.2f".format(centralAngle).toDouble()
    }

    private fun getCircleFlowArea(entity: Structure): Double {
        val diameter = entity.structureDiameter!!
        val percent = entity.flowHeight
        val centralAngle = getCircleCulvertCentralAngle(entity)

        val circularSegmentArea: Double = ((diameter / 2).pow(2.0) * (centralAngle - sin(centralAngle))) / 2

        log.info {
            "ActionLog.CalcUtil.getCentralAngle: diameter = $diameter, percent = $percent, centralAngle = $centralAngle"
        }

        val flowArea = if (percent < 50) {
            circularSegmentArea
        } else {
            Math.PI * (diameter / 2).pow(2.0) - circularSegmentArea
        }
        return "%.2f".format(flowArea).toDouble()
    }

    private fun getBoxFlowArea(entity: Structure): Double {
        val width = entity.structureWidth!!
        val height = entity.structureHeight!!
        val boxFlowArea = "%.2f".format(width * height * entity.flowHeight / 100).toDouble()

        log.info(
            "ActionLog.CalcUtil.getBoxFlowArea: width = {}, height = {}, boxFlowArea = {}",
            width, height, boxFlowArea
        )

        return "%.2f".format(boxFlowArea).toDouble()
    }

    private fun getCircleWettedPerimeter(entity: Structure): Double {
        val radius = entity.structureDiameter!! / 2
        val percent = entity.flowHeight
        val centralAngle = getCircleCulvertCentralAngle(entity)

        log.info(
            "ActionLog.CalcUtil.getCircleWettedPerimeter -> radius = {}, percent = {}, centralAngle = {}",
            radius, percent, centralAngle
        )

        val wettedPerimeter = if (percent < 50) {
            radius * centralAngle
        } else {
            2 * Math.PI * radius - radius * centralAngle
        }

        return "%.2f".format(wettedPerimeter).toDouble()
    }

    private fun getBoxWettedPerimeter(entity: Structure): Double {
        val width = entity.structureWidth!!
        val height = entity.structureHeight!!

        log.info("ActionLog.CalcUtil.getBoxWettedPerimeter: width = {}, height = {}", width, height)

        val boxWettedPerimeter = 2 * height + width

        return "%.2f".format(boxWettedPerimeter).toDouble()
    }

    private fun getCircleHydraulicRadius(entity: Structure): Double {


        val circleCulvertHydraulicRadius = getCircleFlowArea(entity) / getCircleWettedPerimeter(entity)

        log.info("Getting circleCulvert hydraulic radius: End -> {}", circleCulvertHydraulicRadius)

        return "%.2f".format(circleCulvertHydraulicRadius).toDouble()
    }

    private fun getBoxHydraulicRadius(entity: Structure): Double {

        log.info("Getting box hydraulic radius: Start")

        val boxHydraulicRadius = getBoxFlowArea(entity) / getBoxWettedPerimeter(entity)

        log.info("Getting box hydraulic radius: End")

        return "%.2f".format(boxHydraulicRadius).toDouble()
    }

    private fun getCircleWaterSpeed(entity: Structure): Double {
        val slope = entity.slope

        log.info("ActionLog.CalcUtil.getWateerSpeed: calculation of water speed")

        val rad = getCircleHydraulicRadius(entity)
//        val valueN = material.let { Roughness.valueOf(it) }

        val powValue = 0.666 - 0.014 * sqrt(rad) //0.014 - the roughness should be calculated in future

        val waterSpeed = 71.4 * rad.pow(powValue) * sqrt(slope / 100)

        return "%.2f".format(waterSpeed).toDouble()
    }

    private fun getBoxWaterSpeed(entity: Structure): Double {
        val slope = entity.slope

        log.info("ActionLog.CalcUtil.getWateerSpeed: calculation of water speed")

        val rad = getBoxHydraulicRadius(entity)
//        val valueN = material.let { Roughness.valueOf(it) }

        val powValue = 0.666 - 0.014 * sqrt(rad) //0.014 - the roughness should be calculated in future

        val waterSpeed = 71.4 * rad.pow(powValue) * sqrt(slope / 100)

        return "%.2f".format(waterSpeed).toDouble()
    }

    private fun getFlowRate(entity: Structure): Double {
        val flowArea = entity.flowArea!!
        val waterSpeed = entity.waterSpeed!!

        log.info("ActionLog.CalcUtil.getFlowRate: Getting flow rate")

        val flowRate = flowArea * waterSpeed * 1000

        return "%.2f".format(flowRate).toDouble()
    }

    private fun getRequiredFlowRate(entity: Structure): Double {
        val rainIntensity = entity.rainIntensity
        val calculationArea = entity.calculationArea

        log.info("ActionLog.CalcUtil.getRequiredFlowRate: Getting required flow rate")

        val requiredFlowRate = rainIntensity * calculationArea!!

        return "%.2f".format(requiredFlowRate).toDouble()
    }

    private fun getResult(entity: Structure): String {
        val flowRate = entity.flowRate!!
        val requiredFlowRate = entity.requiredFlowRate!!

        log.info("ActionLog.CalcUtil.getResult: result calculated")

        return if (flowRate > requiredFlowRate) {
            FLOW_IS_SATISFIED.toString()
        } else {
            FLOW_FAILED.toString()
        }
    }
}


private fun getMinimumSlope(diameter: Double): Double {
    TODO("WILL BE CALCULATED FOR FUTURE")
}

private fun getValueN(material: String?): Double {
    TODO("WILL BE IMPLEMENTED FOR FUTURE")
//        log.info("Getting value N")

//        return when (material) {
//            "REINFORCED_CONCRETE" -> REINFORCED_CONCRETE.roughness
//            "CORRUGATED_METAL" -> 0.0015
//            "BRICK" -> 0.0016
//            "CERAMIC" -> 0.0017
//            "ASBESTOS_CEMENT" -> 0.0018
//            "cugun" -> 0.0019
//            "STEEL" -> 0.015
//            "PVC" -> 0.0021
//            "asfaltbeton" -> 0.0022
//            "torpaqkanal" -> 0.0023
//            "cinqilkanal" -> 0.0024
//            else -> 0.00
//        }
}