package load

import java.io.File

abstract class Loader {

    fun loadFile(file: File): ModelData = load(file.readBytes())

    abstract fun load(data: ByteArray): ModelData

}