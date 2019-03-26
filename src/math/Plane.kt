package math

import java.lang.Math.abs

class Plane(val positionVector: Vector3, val normalVector: Vector3) {

    fun pointOnPlane(point: Vector3) = pointDistance(point) == 0f

    fun pointAbovePlane(point: Vector3) = pointDistance(point) > 0

    fun pointBelowPlane(point: Vector3) = pointDistance(point) < 0

    fun pointDistance(point: Vector3): Float {
        return normalVector.dot(point - positionVector)
    }

    fun rayPlaneIntersection(ray: Ray): Vector3? {
        if(FloatUtil.floatNE(normalVector.dot(ray.directionVector), 0f)) {
            println(ray)
            val result = normalVector.dot(positionVector - ray.positionVector) / normalVector.dot(ray.directionVector)
            println(ray.directionVector * result + ray.positionVector)
            return ray.directionVector * result + ray.positionVector
        }
        return null
    }

    fun triangleIntersectsPlane(triangle: Triangle): Boolean {
        return when (triangle.vertices.count { pointOnPlane(it) }) {
            0, 1 -> triangle.vertices.count { pointAbovePlane(it) } == 1 || triangle.vertices.count { pointBelowPlane(it) } == 1
            else -> {
                false
            }
        }
    }

}
