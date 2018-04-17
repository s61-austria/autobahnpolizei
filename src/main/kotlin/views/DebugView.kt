package views

import com.kontofahren.connector.Exchange.LOCATION_EXCHANGE
import com.kontofahren.connector.RabbitGateway
import com.kontofahren.connector.Routing.EMPTY
import javafx.scene.layout.VBox
import serializer.LocationUpdateSerializer
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
        (0..100).forEach{ carIds.add(UUID.randomUUID())}
    }
}