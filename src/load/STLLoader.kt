package load

object STLLoader : Loader() {

    override fun load(data: ByteArray): ModelData {
        return if (isAscii(data)) {
            loadAscii(String(data, Charsets.US_ASCII))
        } else {
            loadBinary(BinaryInputStream(data))
        }
    }

    private fun loadBinary(data: BinaryInputStream): ModelData {
        val header = data.readN(80)
        val headerString = String(header).substring(0, header.indexOf(0))
        val triangleCount = data.readInt()

        val vertexData = ModelData(headerString)
        for (i in 0 until triangleCount) {
            val normal = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v1 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v2 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            val v3 = Vector3(data.readFloat(), data.readFloat(), data.readFloat())
            data.readShort()
            vertexData.addFaceWithNormal(v1, v2, v3, normal)
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