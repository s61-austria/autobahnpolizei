package views

import com.google.gson.Gson
import com.kontofahren.connector.Queue
import com.kontofahren.connector.RabbitGateway
import com.lynden.gmapsfx.GoogleMapView
import com.lynden.gmapsfx.MapComponentInitializedListener
import com.lynden.gmapsfx.javascript.`object`.GoogleMap
import com.lynden.gmapsfx.javascript.`object`.LatLong
import com.lynden.gmapsfx.javascript.`object`.MapOptions
import com.lynden.gmapsfx.javascript.`object`.MapTypeIdEnum
import com.lynden.gmapsfx.javascript.`object`.Marker
import com.lynden.gmapsfx.javascript.`object`.MarkerOptions
import javafx.scene.layout.VBox
import serializer.LocationUpdateSerializer
import tornadofx.View
import java.util.UUID


class MapView : View(), MapComponentInitializedListener {
    override val root = VBox()
    override fun mapInitialized() {
        println("Map initialized")

        //Set the initial properties of the map.
        val mapOptions = MapOptions()

        mapOptions.center(LatLong(47.6964719, 13.3457347))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(8)

        map = mapView.createMap(mapOptions)
    }

    lateinit var map: GoogleMap

    val mapView = GoogleMapView().apply {
        addMapInializedListener(this@MapView)
        prefHeight = 700.0
        prefWidth = 1200.0
    }

    val gateway = RabbitGateway()

    var locations = mutableMapOf<UUID, LocationUpdateSerializer>()

    init {
        root.add(mapView)

        gateway.consume(Queue.FRONTEND_LOCATION_UPDATE, {
            val item = Gson().fromJson(it, LocationUpdateSerializer::class.java)
            runAsync {
                locations[UUID.fromString(item.vehicleId)] = item
            } ui {
                updateUi()
            }
        })
    }

    private fun updateUi() {
        locations.map {
            val latlng = LatLong(it.value.lat, it.value.lng)
            val markerOptions = MarkerOptions()
            markerOptions.position(latlng)
            val marker = Marker(markerOptions)
            map.addMarker(marker)
        }
    }
}