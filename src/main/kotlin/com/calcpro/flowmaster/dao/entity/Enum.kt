package com.calcpro.flowmaster.dao.entity

enum class StructureShape(val type: String) {
    CIRCLE_CULVERT("Circle"),
    BOX_CULVERT("Box"),
    TRAPEZOID_CULVERT("Trapezoid"),
    TRIANGLE_CHANNEL("Triangle"),
    RECTANGLE_CHANNEL("Rectangle"),
    TRAPEZOID_CHANNEL("Trapezoid"),
    SEMICIRCULAR_CHANNEL("Semicircular")
}

enum class Roughness(val roughness: Double) {
    POLYETHYLEN_CORRUGATED_INNER_SMOOTH(0.012),
    POLYETHYLEN_CORRUGATED_INNER_CORRUGATED(0.021),
    PVC(0.010),
    ASBESTOS_CEMENT(0.011),
    CAST_IRON(0.012),
    CERAMIC(0.014),
    REINFORCED_CONCRETE(0.011),
    CONCRETE(0.012),
    STEEL(0.012),
    STEEL_INTERNALLY_ENAMELED(0.010),
    CORRUGATED_METAL(0.022),
    GLASS(0.010),
    LEAD(0.011),
    BRASS(0.011),
    COPPER(0.011),
    PLASTIC(0.009)
}

enum class Result {
    FLOW_IS_SATISFIED,
    FLOW_FAILED
}