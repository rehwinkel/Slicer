package math

data class Triangle(val v1: Vector3, val v2: Vector3, val v3: Vector3, val normal: Vector3 = calculateNormal(v1, v2, v3)) {

    companion object {
        private fun calculateNormal(v1: Vector3, v2: Vector3, v3: Vector3): Vector3 {
            val v = v2.sub(v1)
            val w = v3.sub(v1)
            return v.cross(w).normalized()
        }
    }

    val vertices: Collection<Vector3>
        get() = listOf(v1, v2, v3)

}
