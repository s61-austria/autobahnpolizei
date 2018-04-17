package views

import com.lynden.gmapsfx.GoogleMapView
import com.lynden.gmapsfx.MapComponentInitializedListener
import com.lynden.gmapsfx.javascript.`object`.GoogleMap
import com.lynden.gmapsfx.javascript.`object`.LatLong
import com.lynden.gmapsfx.javascript.`object`.MapOptions
import com.lynden.gmapsfx.javascript.`object`.MapTypeIdEnum
import javafx.scene.layout.VBox
import tornadofx.View


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

    init {
        root.add(mapView)
    }
}