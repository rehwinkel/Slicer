package load

class ModelData(val header: String) {
    val faces = ArrayList<Face>()

    fun addFaceWithNormal(v1: Vector3, v2: Vector3, v3: Vector3, normal: Vector3) {
        add(Face(v1, v2, v3, normal))
    }

    fun add(face: ModelData.Face) {
        faces.add(face)
    }

    data class Face(val v1: Vector3, val v2: Vector3, val v3: Vector3, val normal: Vector3)

    override fun toString() = "ModelData(count=${faces.size}, data=$faces)"
}