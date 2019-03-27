package math

data class Triangle(val v1: Vector3, val v2: Vector3, val v3: Vector3, val normal: Vector3 = calculateNormal(v1, v2, v3)) {

    companion object {
        private fun calculateNormal(v1: Vector3, v2: Vector3, v3: Vector3): Vector3 {
            val v = v2 - v1
            val w = v3 - v1
            return v.cross(w).normalized()
        }
    }

    val vertices: Collection<Vector3>
        get() = listOf(v1, v2, v3)

    fun planeIntersection(plane: Plane): Pair<Vector3, Vector3>? {
        when (vertices.count { plane.pointOnPlane(it) }) {
            1 -> {
                val verts = ArrayList(vertices)
                val p1 = verts.removeAt(verts.indexOfFirst { plane.pointOnPlane(it) })

                val root: Vector3
                if (verts.count { plane.pointAbovePlane(it) } == 1) {
                    root = verts.removeAt(verts.indexOfFirst { plane.pointAbovePlane(it) })
                } else if (verts.count { plane.pointBelowPlane(it) } == 1) {
                    root = verts.removeAt(verts.indexOfFirst { plane.pointBelowPlane(it) })
                } else {
                    return null
                }

                val r1 = Ray(root, (root - verts.removeAt(0)).normalized())
                val p2 = r1.planeIntersection(plane)
                return p2?.let { Pair(p1, p2) }
            }
            0 -> {
                val verts = ArrayList(vertices)
                val rootIndex = if (verts.count { plane.pointAbovePlane(it) } == 1) {
                    verts.indexOfFirst { plane.pointAbovePlane(it) }
                } else if (verts.count { plane.pointBelowPlane(it) } == 1) {
                    verts.indexOfFirst { plane.pointBelowPlane(it) }
                } else {
                    return null
                }

                val root = verts.removeAt(rootIndex)
                val r1 = Ray(root, (root - verts.removeAt(0)).normalized())
                val r2 = Ray(root, (root - verts.removeAt(0)).normalized())
                val p1 = r1.planeIntersection(plane)
                val p2 = r2.planeIntersection(plane)
                return p1?.let { p2?.let { Pair(p1, p2) } }
            }
            else -> return null
        }
    }

}
