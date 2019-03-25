package math

import java.lang.Math.abs

object FloatUtil {

    private val epsilon = 5.96e-7

    fun floatEQ(a: Float, b: Float): Boolean {
        return abs(a-b) < epsilon
    }

    fun floatNE(a: Float, b: Float): Boolean {
        return !floatEQ(a, b)
    }

}