package load

import math.Triangle
import math.Vector3

object OBJLoader : Loader() {
    override fun save(data: ModelData): ByteArray {
        val vertices = data.faces.flatMap { it.vertices }
        val normals = data.faces.map { it.normal }
        val normalsSet = normals.distinct().mapIndexed { i, v -> v to i }.toMap()
        val normalsIndexMapping = normals.mapIndexed { i, v -> i to normalsSet.getValue(v) }.toMap()
        val verticesSet = vertices.distinct().mapIndexed { i, v -> v to i }.toMap()
        val verticesIndexMapping = vertices.mapIndexed { i, v -> i to verticesSet.getValue(v) }.toMap()

        val outStream = StringBuilder()
        outStream.append("o ${data.header}\n")
        verticesSet.keys.forEach {
            outStream.append("v ${it.x} ${it.y} ${it.z}\n")
        }
        normalsSet.keys.forEach {
            outStream.append("vn ${it.x} ${it.y} ${it.z}\n")
        }

        data.faces.forEachIndexed { index, triangle ->
            val realX = verticesIndexMapping.getValue(index * 3 + 0) + 1
            val realY = verticesIndexMapping.getValue(index * 3 + 1) + 1
            val realZ = verticesIndexMapping.getValue(index * 3 + 2) + 1
            val normal = normalsIndexMapping.getValue(index) + 1
            outStream.append("f $realX//$normal $realY//$normal $realZ//$normal\n")
        }

        return outStream.toString().toByteArray(Charsets.US_ASCII)
    }

    override fun load(data: ByteArray): ModelData {
        val text = String(data)
        val lines = text.split("\n")

        val vertices = ArrayList<Vector3>()
        val normals = ArrayList<Vector3>()

        val vertexData = ModelData()
        for (line in lines) {
            when {
                line.startsWith("o ") -> {
                    vertexData.header = line.substring(2)
                }
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

                    val triangle = if(faceNormalIndices.distinct().size == 1) {
                        Triangle(faceVertices[0], faceVertices[1], faceVertices[2], normals[faceNormalIndices.first()])
                    } else {
                        Triangle(faceVertices[0], faceVertices[1], faceVertices[2])
                    }
                    vertexData.add(triangle)
                }
            }
        }
        if (vertices.size == 0) {
            throw RuntimeException("Found 0 vertices!")
        }
        return vertexData
    }

}