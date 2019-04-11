package math

import math.FloatUtil.floatNE

data class Vector3(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {
    val xz: Vector2
        get() = Vector2(x, z)

    fun normalized(): Vector3 {
        val length = mag()
        return Vector3(this.x / length, this.y / length, this.z / length)
    }

    fun cross(b: Vector3): Vector3 {
        return Vector3(this.y * b.z - this.z * b.y, this.z * b.x - this.x * b.z, this.x * b.y - this.y * b.x)
    }

    fun dot(b: Vector3): Float {
        return this.x * b.x + this.y * b.y + this.z * b.z
    }

    fun mag(): Float {
        return Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z).toDouble()).toFloat()
    }

    fun swapYZ(): Vector3 {
        return Vector3(this.x, this.z, this.y)
    }

    operator fun plus(other: Vector3): Vector3  {
        return Vector3(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector3

        if (floatNE(x, other.x)) return false
        if (floatNE(y, other.y)) return false
        if (floatNE(z, other.z)) return false

        return true
    }

    operator fun times(b: Float): Vector3 {
        return Vector3(this.x * b, this.y * b, this.z * b)
    }

    operator fun times(other: Vector3): Vector3 {
        return Vector3(this.x * other.x, this.y * other.y, this.z * other.z)
    }
}