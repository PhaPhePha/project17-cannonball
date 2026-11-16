package edu.project17.cannonball

//luu kq mo phong va cac diem tren duong bay
data class FlightResult(
    val angleDeg: Double,
    val landingDistance: Double,
    val flightTime: Double,
    val points: List<Pair<Double, Double>>,
)
