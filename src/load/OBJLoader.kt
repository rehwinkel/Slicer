package load

object OBJLoader : Loader() {

    override fun load(data: ByteArray): ModelData {
        val text = String(data)
        val lines = text.split("\n")

        val vertices = ArrayList<Vector3>()
        val normals = ArrayList<Vector3>()

        val vertexData = ModelData("")
        for (line in lines) {
            when {
                line.startsWith("v ") -> {
                    val vd = line.substring(2).split(" ").map { it.toFloat() }
                    vertices.add(Vector3(vd[0], vd[1], vd[2]))
                }
                line.startsWith("vn ") -> {
                    val vnd = line.substring(3).split(" ").map { it.toFloat() }
                    normals.add(Vector3(vnd[0], vnd[1], vnd[2]))
                }
                line.startsWith("f ") -> {
                    val faceVertices = ArrayList<Vector3>()
                    val faceNormalIndices = ArrayList<Int>()
                    line.substring(2).split(" ").forEach {
                        when(it.length - it.replace("/", "").length) {
                            2 -> {
                                val vertexIndex: Int
                                val normalIndex: Int
                                it.split("/").let { i ->
                                    vertexIndex = i[0].toInt() - 1
                                    normalIndex = i[2].toInt() - 1
                                }
                                faceVertices.add(vertices[vertexIndex])
                                faceNormalIndices.add(normalIndex)
                            }
                            0, 1 -> {
                                val vertexIndex = if ("/" in it) {
                                    it.split("/")[0].toInt() - 1
                                } else {
                                    it.toInt() - 1
                                }
                                faceVertices.add(vertices[vertexIndex])
                            }
                        }
                    }

                    val faceNormal = if(faceNormalIndices.distinct().size == 1) {
                        normals[faceNormalIndices.first()]
                    } else {
                        val v = faceVertices[1].sub(faceVertices[0])
                        val w = faceVertices[2].sub(faceVertices[0])
                        v.cross(w).normalized()
                    }
                    vertexData.addFaceWithNormal(faceVertices[0], faceVertices[1], faceVertices[2], faceNormal)
                }
            }
        }
        if (vertices.size == 0) {
            throw RuntimeException("Found 0 vertices!")
        }
        return vertexData
    }

}