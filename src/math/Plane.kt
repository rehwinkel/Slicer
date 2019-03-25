package math

class Plane(val positionVector: Vector3, val normalVector: Vector3) {

    fun pointOnPlane(point: Vector3) = pointDistance(point) == 0f

    fun pointAbovePlane(point: Vector3) = pointDistance(point) > 0

    fun pointBelowPlane(point: Vector3) = pointDistance(point) < 0

    fun pointDistance(point: Vector3): Float {
        return normalVector.dot(point.sub(positionVector))
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
