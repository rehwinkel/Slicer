package load

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
        return ByteBuffer.wrap(byteArrayOf(readByte(), readByte())).order(ByteOrder.LITTLE_ENDIAN).short
    }

    fun readInt(): Int {
        return ByteBuffer.wrap(byteArrayOf(readByte(), readByte(), readByte(), readByte())).order(ByteOrder.LITTLE_ENDIAN).int
    }

    fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    fun readN(amount: Int): ByteArray {
        return (0 until amount).map { readByte() }.toByteArray()
    }

}