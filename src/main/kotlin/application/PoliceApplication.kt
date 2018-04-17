package application

import javafx.application.Application
import javafx.scene.text.FontWeight
import tornadofx.App
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.px
import views.MapView

fun main(args: Array<String>) {
    Application.launch(PoliceApplication::class.java, *args)
}

open class PoliceApplication : App(MapView::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}