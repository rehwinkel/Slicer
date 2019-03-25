package load

data class Vector3(val x: Float, val y: Float, val z: Float) {
    fun add(other: Vector3): Vector3 {
        return Vector3(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    fun normalized(): Vector3 {
        val length = Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z).toDouble()).toFloat()
        return Vector3(this.x / length, this.y / length, this.z / length)
    }

    fun cross(b: Vector3): Vector3 {
        val a = this
        return Vector3(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x)
    }

    fun sub(other: Vector3): Vector3 {
        return Vector3(this.x - other.x, this.y - other.y, this.z - other.z)
    }
}