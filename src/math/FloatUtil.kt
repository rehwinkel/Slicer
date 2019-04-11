package math

import java.lang.Math.abs

object FloatUtil {

    const val epsilon = 1e-6

    fun floatEQ(a: Float, b: Float): Boolean {
        return abs(a-b) < epsilon
    }

    fun floatNE(a: Float, b: Float): Boolean {
        return !floatEQ(a, b)
    }

}