package main

import load.OBJLoader
import load.STLLoader
import load.ModelData
import org.lwjgl.opengl.GL11
import render.Window
import java.io.File

fun main() {
    //println(loader.loadFile("/home/ian/Desktop/cube_ascii.stl"))
    //println(loader.loadFile("/home/ian/Desktop/cube_bin.stl"))
    //println(loader.loadFile("/home/ian/Desktop/Sphericon.stl"))
    //println(loader.loadFile("/home/ian/Desktop/tea.stl"))
    render(OBJLoader.loadFile(File("/home/ian/Desktop/testmodels/testball.obj")))
    render(STLLoader.loadFile(File("/home/ian/Desktop/testmodels/testmodel.stl")))
    //render(OBJLoader.loadFile(File("/home/ian/Desktop/testmodels/testball2.obj")))
    //render(STLLoader.loadFile(File("/home/ian/Desktop/testmodels/testmodel.stl")))
}

fun render(model: ModelData) {
    Window.globalInit()

    var angle = 0f

    val window = Window(640, 640, "Render STL Window")
    window.init {
        GL11.glClearColor(0.1f, 0.3f, 0.8f, 1.0f)
    }

    window.mainloop {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        GL11.glLoadIdentity()
        GL11.glPushMatrix()
        GL11.glRotatef(angle, 1f, 0f, 0f)
        GL11.glBegin(GL11.GL_TRIANGLES)
        model.faces.forEach {
            GL11.glColor3f(it.normal.x, it.normal.y, it.normal.z)
            GL11.glVertex3f(it.v1.x / 3, it.v1.y / 3, it.v1.z / 3)
            GL11.glVertex3f(it.v2.x / 3, it.v2.y / 3, it.v2.z / 3)
            GL11.glVertex3f(it.v3.x / 3, it.v3.y / 3, it.v3.z / 3)
        }
        GL11.glEnd()
        GL11.glPopMatrix()

        angle += 0.5f
    }
}