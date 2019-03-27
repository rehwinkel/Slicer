package load

import math.Vector3

object STLLoader : Loader() {

    override fun save(data: ModelData): ByteArray {
        val outStream = BinaryOutputStream()
        val headerBytes = data.header.toByteArray(Charsets.US_ASCII)
        outStream.write(headerBytes)
        outStream.write(ByteArray(80 - headerBytes.size))

        outStream.writeInt(data.faces.size)
        data.faces.forEach {
            val n = it.normal.swapYZ()
            outStream.writeFloat(n.x)
            outStream.writeFloat(n.y)
            outStream.writeFloat(n.z)
            it.vertices.forEach { j ->
                val i = j.swapYZ()
                outStream.writeFloat(i.x)
                outStream.writeFloat(i.z)
                outStream.writeFloat(i.y)
            }
            outStream.writeShort(2)
        }

        return outStream.toByteArray()
    }

    override fun load(data: ByteArray): ModelData {
        return if (isAscii(data)) {
            loadAscii(String(data, Charsets.US_ASCII))
        } else {
            loadBinary(BinaryInputStream(data))
        }
    }

    private fun loadBinary(data: BinaryInputStream): ModelData {
        val header = data.readN(80)
        val headerString = String(header, Charsets.US_ASCII).substring(0, header.indexOf(0))
        val triangleCount = data.readInt()

        val vertexData = ModelData(headerString)
        for (i in 0 until triangleCount) {
            val normal = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v1 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v2 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v3 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            data.readShort()
            vertexData.addFaceWithNormal(v1.swapYZ(), v2.swapYZ(), v3.swapYZ(), normal.swapYZ())
        }
        return vertexData
    }

    private fun loadAscii(asciiData: String): ModelData {
        val parser = STLParser(asciiData)
        return parser.parse()
    }

    private fun isAscii(bytes: ByteArray): Boolean {
        return bytes.all {
            it in 0..127
        }
    }

}