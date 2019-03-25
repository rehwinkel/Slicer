package render

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

class Window(val width: Int, val height: Int, val title: String) {
    var window: Long = 0

    companion object {
        fun globalInit() {
            if (!GLFW.glfwInit()) {
                throw RuntimeException("GLFW didn't Initialize")
            }
        }
    }

    fun init(initCallback: () -> Unit) {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0)
        if (window == 0L) {
            throw RuntimeException("Window didn't Initialize")
        }
        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSwapInterval(1)
        GLFW.glfwShowWindow(window)
        GL.createCapabilities()
        initCallback()
    }

    fun mainloop(renderCallback: () -> Unit) {
        while (!GLFW.glfwWindowShouldClose(window)) {
            renderCallback()
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
        }

        GLFW.glfwDestroyWindow(window)
        GLFW.glfwTerminate()
    }

}