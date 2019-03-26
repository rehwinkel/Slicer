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
        if (FloatUtil.floatNE(normalVector.dot(ray.directionVector), 0f, FloatUtil.softEpsilon)) {
            val result = normalVector.dot(positionVector - ray.positionVector) / normalVector.dot(ray.directionVector)
            if ((ray.directionVector * result + ray.positionVector).mag() > 1000) {
                println("${abs(normalVector.dot(ray.directionVector)) < FloatUtil.softEpsilon}")
                println(ray)
                println(ray.directionVector * result + ray.positionVector)
            }
            return ray.directionVector * result + ray.positionVector
        }
        return null
    }

    fun triangleIntersectsPlane(triangle: Triangle): Pair<Vector3, Vector3>? {
        when (triangle.vertices.count { pointOnPlane(it) }) {
            1 -> {
                val verts = ArrayList(triangle.vertices)
                val p1 = verts.removeAt(verts.indexOfFirst { pointOnPlane(it) })

                val root: Vector3
                if (verts.count { pointAbovePlane(it) } == 1) {
                    root = verts.removeAt(verts.indexOfFirst { pointAbovePlane(it) })
                } else if (verts.count { pointBelowPlane(it) } == 1) {
                    root = verts.removeAt(verts.indexOfFirst { pointBelowPlane(it) })
                } else {
                    return null
                }

                val r1 = Ray(root, (root - verts.removeAt(0)).normalized())
                val p2 = rayPlaneIntersection(r1)
                return p2?.let { Pair(p1, p2) }
            }
            0 -> {
                val verts = ArrayList(triangle.vertices)
                val rootIndex = if (verts.count { pointAbovePlane(it) } == 1) {
                    verts.indexOfFirst { pointAbovePlane(it) }
                } else if (verts.count { pointBelowPlane(it) } == 1) {
                    verts.indexOfFirst { pointBelowPlane(it) }
                } else {
                    return null
                }

                val root = verts.removeAt(rootIndex)
                val r1 = Ray(root, (root - verts.removeAt(0)).normalized())
                val r2 = Ray(root, (root - verts.removeAt(0)).normalized())
                val p1 = rayPlaneIntersection(r1)
                val p2 = rayPlaneIntersection(r2)
                return p1?.let { p2?.let { Pair(p1, p2) } }
            }
            else -> return null
        }
    }

}
