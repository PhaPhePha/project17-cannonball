package edu.project17.cannonball

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PhysicsTest {
    @Test
    fun `landing distance matches reference values`() {
        val distance = Physics.landingDistance(30.0, 45.0, 2.0)

        assertEquals(93.7013, distance, 1e-3)
    }

    @Test
    fun `simulation starts at launch point and ends on ground`() {
        val result = Physics.simulate(30.0, 45.0, 2.0)

        assertEquals(0.0, result.points.first().first, 1e-9)
        assertEquals(2.0, result.points.first().second, 1e-9)
        assertEquals(0.0, result.points.last().second, 1e-9)
        assertEquals(result.landingDistance, result.points.last().first, 1e-9)
    }

    @Test
    fun `bisection finds the low angle for a reachable target`() {
        val angle = Physics.solveAngle(30.0, 2.0, 70.0)

        assertNotNull(angle)
        assertEquals(22.7985, angle!!, 1e-3)
    }

    @Test
    fun `bisection finds a target close to ninety degrees`() {
        val angle = Physics.solveAngle(30.0, 2.0, 0.001)

        assertNotNull(angle)
        assertTrue(angle!! > 89.0)
    }

    @Test
    fun `bisection rejects a target outside the maximum range`() {
        assertNull(Physics.solveAngle(5.0, 0.0, 1000.0))
    }
}
