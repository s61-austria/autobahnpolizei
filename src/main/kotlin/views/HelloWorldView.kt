package views

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.View

class HelloWorldView : View() {
    override val root = VBox()

    init {
        root.add(Label("Label"))
    }
}