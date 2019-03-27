package math

import java.lang.Math.abs

object FloatUtil {

    const val epsilon = 5.96e-7
    const val softEpsilon = 5.96e-5

    fun floatEQ(a: Float, b: Float, eps: Double = epsilon): Boolean {
        return abs(a-b) < epsilon
    }

    fun floatNE(a: Float, b: Float, eps: Double = epsilon): Boolean {
        return !floatEQ(a, b, eps)
    }

}