package main

import load.ModelData
import load.STLLoader
import math.*
import org.lwjgl.opengl.GL11
import testgl.Window
import java.io.File
import kotlin.math.min

fun main() {
    val model = STLLoader.loadFile(File("/home/ian/Desktop/testmodels/tea.stl"))
    //val model = OBJLoader.loadFile(File("/home/ian/Desktop/testmodels/cube.obj"))
    //val model = STLLoader.loadFile(File("/home/ian/Desktop/testmodels/2cubs.stl"))

    val lineverts = ArrayList<Line>()

    val yPlane = Plane(Vector3(0f, 3f, 0f), Vector3(0f, 1f, 0f))
    val intersecting = model.faces.mapNotNull {
        it.planeIntersection(yPlane)
    }
    intersecting.forEach {
        lineverts.add(Line(it.first.xz, it.second.xz))
    }

    val saps = getShapes(lineverts)
    renderShapes(saps)
}

data class Shape(val points: List<Vector2>, val closed: Boolean)

fun getShapes(lines: ArrayList<Line>): List<Shape> {
    var currentLine = lines.removeAt(2)
    val shapes = ArrayList<Shape>()
    val points = ArrayList<Vector2>()

    var search = true

    while (true) {
        val p = if (!search) {
            currentLine.pointB
        } else {
            currentLine.pointA
        }
        val q = if (search) {
            currentLine.pointB
        } else {
            currentLine.pointA
        }
        points.add(q)

        if (lines.isEmpty()) {
            val closed = if ((p - points[0]).mag() < FloatUtil.epsilon) {
                true
            } else {
                points.add(p)
                false
            }
            shapes.add(Shape(points, closed))
            break
        }

        currentLine = lines.minBy { min((it.pointA - p).mag(), (it.pointB - p).mag()) }!!
        lines.remove(currentLine)

        val d0 = (currentLine.pointA - p).mag()
        val d1 = (currentLine.pointB - p).mag()
        search = d1 < d0

        val d = min(d0, d1)
        if (d > FloatUtil.epsilon) {
            val closed = if ((p - points[0]).mag() < FloatUtil.epsilon) {
                true
            } else {
                points.add(p)
                false
            }
            shapes.add(Shape(ArrayList(points), closed))
            points.clear()
        }
    }

    return shapes
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

fun renderShapes(shapes: List<Shape>) {
    Window.globalInit()

    val window = Window(1000, 1000, "Render STL Window")
    window.init {
        GL11.glClearColor(0.1f, 0.3f, 0.8f, 1.0f)
    }

    val f = 0.13f

    window.mainloop {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        for (shape in shapes) {
            val verts = shape.points
            GL11.glBegin(GL11.GL_LINES)
            for (i in 0 until verts.size - 1) {
                val it = verts[i]
                GL11.glVertex2f(it.x * f, it.y * f)
                val it2 = verts[i + 1]
                GL11.glVertex2f(it2.x * f, it2.y * f)
            }
            if (shape.closed) {
                verts.last().let {
                    GL11.glVertex2f(it.x * f, it.y * f)
                }
                verts.first().let {
                    GL11.glVertex2f(it.x * f, it.y * f)
                }
            }
            GL11.glEnd()
        }
    }
}