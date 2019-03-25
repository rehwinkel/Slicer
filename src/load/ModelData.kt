package load

import math.Triangle
import math.Vector3

class ModelData(var header: String = "") {
    val faces = ArrayList<Triangle>()

    fun addFaceWithNormal(v1: Vector3, v2: Vector3, v3: Vector3, normal: Vector3) {
        add(Triangle(v1, v2, v3, normal))
    }

    fun add(face: Triangle) {
        faces.add(face)
    }

    override fun toString() = "ModelData(count=${faces.size}, data=$faces)"
}