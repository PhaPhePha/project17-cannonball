package edu.project17.cannonball

import kotlin.math.*

//gia toc trong truong trog mo phong
private const val G = 9.81

//nhom cac ham vat ly lien quan den chuyen dong nem xien
object Physics {
    //tinh kcach vien dan bay dc
    fun landingDistance(speed: Double, angleDeg: Double, height: Double): Double {
        val angle = Math.toRadians(angleDeg)
        val vertical = speed * sin(angle)
        val time = (vertical + sqrt(vertical * vertical + 2.0 * G * height)) / G
        return speed * cos(angle) * time
    }

    //mo phogn quy dao theo tung moc thoi gian
    fun simulate(speed: Double, angleDeg: Double, height: Double): FlightResult {
        val angle = Math.toRadians(angleDeg)
        val vertical = speed * sin(angle)
        val time = (vertical + sqrt(vertical * vertical + 2.0 * G * height)) / G
        val points = (0..100).map { i ->
            val t = time * i / 100.0
            Pair(speed * cos(angle) * t, height + vertical * t - 0.5 * G * t * t)
        }
        return FlightResult(angleDeg, points.last().first, time, points)
    }

    //tim goc phu hop = pp chia doi
    fun solveAngle(speed: Double, height: Double, target: Double): Double? {
        val lo = 0.0
        val hi = 90.0

        //Tim cuc dai, cuc tieu bang tkiem tam phan
        var peakLo = lo
        var peakHi = hi
        repeat(100) {
            val left = peakLo + (peakHi - peakLo) / 3.0
            val right = peakHi - (peakHi - peakLo) / 3.0
            if (landingDistance(speed, left, height) < landingDistance(speed, right, height)) {
                peakLo = left
            } else {
                peakHi = right
            }
        }
        val peakAngle = (peakLo + peakHi) / 2.0
        val maxDistance = landingDistance(speed, peakAngle, height)
        if (target > maxDistance + 1e-7) return null

        fun errorAt(angle: Double) = landingDistance(speed, angle, height) - target
        fun isBracketed(left: Double, right: Double): Boolean =
            left == 0.0 || right == 0.0 ||
                (left < 0.0 && right > 0.0) || (left > 0.0 && right < 0.0)

        var a = lo
        var b = peakAngle
        var fa = errorAt(a)
        var fb = errorAt(b)
        if (abs(fa) < 1e-7) return a
        if (abs(fb) < 1e-7) return b
        if (!isBracketed(fa, fb)) {
            a = peakAngle
            b = hi
            fa = errorAt(a)
            fb = errorAt(b)
        }
        if (!isBracketed(fa, fb)) return null
        repeat(100) {
            val mid = (a + b) / 2.0
            val fm = landingDistance(speed, mid, height) - target
            if (abs(fm) < 1e-8 || abs(b - a) < 1e-8) return mid
            if (fa * fm <= 0) { b = mid; fb = fm } else { a = mid; fa = fm }
        }
        return (a + b) / 2.0
    }
}
