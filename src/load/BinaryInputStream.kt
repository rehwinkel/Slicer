package load

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

class BinaryInputStream(data: ByteArray) : ByteArrayInputStream(data) {

    fun readByte(): Byte {
        val readData = read()
        return if (readData != -1) {
            readData.toByte()
        } else {
            throw RuntimeException("EOF")
        }
    }

    fun readShort(): Short {
        val b0 = readByte()
        val b1 = readByte()
        return ByteBuffer.wrap(byteArrayOf(b1, b0)).short
    }

    fun readInt(): Int {
        val b0 = readByte()
        val b1 = readByte()
        val b2 = readByte()
        val b3 = readByte()
        return ByteBuffer.wrap(byteArrayOf(b3, b2, b1, b0)).int
    }

    fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    fun readN(amount: Int): ByteArray {
        return (0 until amount).map { readByte() }.toByteArray()
    }

}