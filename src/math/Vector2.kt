package math

import math.FloatUtil.floatNE

data class Vector2(val x: Float = 0f, val y: Float = 0f) {
    fun normalized(): Vector2 {
        val length = mag()
        return Vector2(this.x / length, this.y / length)
    }

    fun dot(b: Vector2): Float {
        return this.x * b.x + this.y * b.y
    }

    fun mag(): Float {
        return Math.sqrt((this.x * this.x + this.y * this.y).toDouble()).toFloat()
    }

    operator fun plus(other: Vector2): Vector2 {
        return Vector2(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vector2): Vector2 {
        return Vector2(this.x - other.x, this.y - other.y)
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector2

        if (floatNE(x, other.x)) return false
        if (floatNE(y, other.y)) return false

        return true
    }

    operator fun times(b: Float): Vector2 {
        return Vector2(this.x * b, this.y * b)
    }

    operator fun times(other: Vector2): Vector2 {
        return Vector2(this.x * other.x, this.y * other.y)
    }
}