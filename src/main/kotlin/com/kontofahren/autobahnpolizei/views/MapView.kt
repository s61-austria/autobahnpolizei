package com.kontofahren.autobahnpolizei.views

import com.google.gson.Gson
import com.kontofahren.integrationslosung.Exchange.LOCATION_EXCHANGE
import com.kontofahren.integrationslosung.RabbitGateway
import com.kontofahren.datenvertrag.LocationUpdateSerializer
import com.lynden.gmapsfx.GoogleMapView
import com.lynden.gmapsfx.MapComponentInitializedListener
import com.lynden.gmapsfx.javascript.`object`.GoogleMap
import com.lynden.gmapsfx.javascript.`object`.LatLong
import com.lynden.gmapsfx.javascript.`object`.MapOptions
import com.lynden.gmapsfx.javascript.`object`.MapTypeIdEnum
import com.lynden.gmapsfx.javascript.`object`.Marker
import com.lynden.gmapsfx.javascript.`object`.MarkerOptions
import javafx.scene.layout.VBox
import tornadofx.View
import java.util.UUID


class MapView : View(), MapComponentInitializedListener {
    val DEBUG = true
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

        debugView.start()
    }

    lateinit var map: GoogleMap

    val mapView = GoogleMapView().apply {
        addMapInializedListener(this@MapView)
        prefHeight = 700.0
        prefWidth = 1200.0
    }
    val debugView = DebugView()

    val gateway = RabbitGateway()

    var locations = mutableMapOf<UUID, LocationUpdateSerializer>()
    var markers = mutableMapOf<UUID, Marker>()

    init {
        root.add(mapView)
        root.add(debugView)

        val queue = gateway.createExclusiveQueue(LOCATION_EXCHANGE)

        gateway.consume(queue, {
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
            val markerOptions = MarkerOptions()
            val latlng = LatLong(it.value.lat, it.value.lng)

            val marker = markers[it.key] ?: Marker(markerOptions).apply {
                markers[it.key] = this
                map.addMarker(this)
            }

            marker.setPosition(latlng)
        }
    }
}