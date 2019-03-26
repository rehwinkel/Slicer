package math

data class Ray(val positionVector: Vector3, val directionVector: Vector3) {

    fun planeIntersection(plane: Plane): Vector3? {
        if (FloatUtil.floatNE(plane.normalVector.dot(directionVector), 0f)) {
            val result = plane.normalVector.dot(positionVector - positionVector) / plane.normalVector.dot(directionVector)
            return directionVector * result + positionVector
        }
        return null
    }

}
