package com.kontofahren.autobahnpolizei.views

import com.kontofahren.datenvertrag.LocationUpdateSerializer
import com.kontofahren.integrationslosung.Exchange.LOCATION_EXCHANGE
import com.kontofahren.integrationslosung.RabbitGateway
import com.kontofahren.integrationslosung.Routing.EMPTY
import javafx.scene.layout.VBox
import tornadofx.View
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.timer

class DebugView : View() {
    override val root = VBox()
    val gateway = RabbitGateway()
    val debug = true
    val carIds = mutableListOf<UUID>()
    fun randomLat() = ThreadLocalRandom.current().nextDouble(42.5, 57.5)
    fun randomLng() = ThreadLocalRandom.current().nextDouble(15.0, 20.0)

    fun start() {
        if (debug) {
            timer("debug", true, 1000, 1000, {
                println("Publishing location")
                val location = LocationUpdateSerializer(
                        carIds.shuffled().first().toString(),
                        randomLat(),
                        randomLng(),
                        ""
                )
                gateway.publish(LOCATION_EXCHANGE, location, EMPTY)
            })
        }
    }

    init {
        (0..100).forEach { carIds.add(UUID.randomUUID()) }
    }
}