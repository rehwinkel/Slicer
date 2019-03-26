package main

import load.ModelData
import load.OBJLoader
import load.STLLoader
import math.Plane
import math.Triangle
import math.Vector3
import org.lwjgl.opengl.GL11
import testgl.Window
import java.io.File

fun main(args: Array<String>) {
    //println(loader.loadFile("/home/ian/Desktop/cube_ascii.stl"))
    //println(loader.loadFile("/home/ian/Desktop/cube_bin.stl"))
    //println(loader.loadFile("/home/ian/Desktop/Sphericon.stl"))
    //println(loader.loadFile("/home/ian/Desktop/tea.stl"))
    val model = STLLoader.loadFile(File("/home/ian/Desktop/testmodels/tea.stl"))
    //val model = STLLoader.loadFile(File("/home/ian/Desktop/testmodels/cube_ascii.stl"))
    //val model = OBJLoader.loadFile(File("/home/ian/Desktop/testmodels/futebol.obj"))
    var height = -6f
    val data = ModelData(model.header)
    for (i in 0..240) {
        val yPlane = Plane(Vector3(0f, height, 0f), Vector3(0f, 1f, 0f))
        val intersecting = model.faces.mapNotNull { yPlane.triangleIntersectsPlane(it) }
        intersecting.forEach {
            data.add(Triangle(it.first, it.first, it.second))
        }
        height += 0.05f
        println("Slicing layer $i!")
    }
    println("Sliced!")
    OBJLoader.saveFile(File("/home/ian/Desktop/out.obj"), data)
    //render(model)
    //testgl.render(OBJLoader.loadFile(File("/home/ian/Desktop/testmodels/testball2.obj")))
    //testgl.render(STLLoader.loadFile(File("/home/ian/Desktop/testmodels/testmodel.stl")))
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

//I was here :D