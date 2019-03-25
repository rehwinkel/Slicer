package load

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BinaryOutputStream : ByteArrayOutputStream() {

    fun writeFloat(f: Float) {
        writeInt(f.toBits())
    }

    fun writeInt(i: Int) {
        write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array())
    }

    fun writeShort(s: Short) {
        write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).array())
    }

}