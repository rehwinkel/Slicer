package math

class Plane(val positionVector: Vector3, val normalVector: Vector3) {

    fun pointOnPlane(point: Vector3) = pointDistance(point) == 0f

    fun pointAbovePlane(point: Vector3) = pointDistance(point) > 0

    fun pointBelowPlane(point: Vector3) = pointDistance(point) < 0

    fun pointDistance(point: Vector3): Float {
        return normalVector.dot(point - positionVector)
    }

}
