package main

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class SlicerApp : Application() {

    override fun start(primaryStage: Stage) {
        val root: Parent = FXMLLoader.load(javaClass.getResource("/layout.fxml"))
        primaryStage.title = "root"
        primaryStage.scene = Scene(root, 640.0, 480.0)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SlicerApp::class.java, *args)
        }
    }

}
