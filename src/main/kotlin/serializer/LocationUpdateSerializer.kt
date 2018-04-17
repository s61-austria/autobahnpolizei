package serializer

data class LocationUpdateSerializer (
        val vehicleId: String,
        val lat: Double,
        val lng: Double,
        val locationId: String?
)