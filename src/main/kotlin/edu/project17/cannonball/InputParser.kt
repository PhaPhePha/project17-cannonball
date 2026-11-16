package edu.project17.cannonball

object InputParser {
    fun parse(text: String, errorMessage: String): Double =
        text.trim().replace(',', '.').toDoubleOrNull() ?: error(errorMessage)
}
