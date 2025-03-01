package com.example.battletanks.utils

import android.view.View
import com.example.battletanks.CEll_SIZE
import com.example.battletanks.binding
import com.example.battletanks.models.Coordinate
import com.example.battletanks.models.Element

fun View.checkViewCanMoveThrounghBorder(coordinate: Coordinate): Boolean {
    return coordinate.top >= 0 &&
            coordinate.top+ this.height <= binding.container.height &&
            coordinate.left >= 0 &&
            coordinate.left+ this.width <= binding.container.width
}

fun getElementByCoordinates(
    coordinate: Coordinate,
    elementsOnContainer: List<Element>
): Element? {
    for (element in elementsOnContainer) {
        for (height in 0 until element.height) {
            for (width in 0 until element.width) {
                val searchingCoordinate = Coordinate(
                    top = element.coordinate.top + height * CEll_SIZE,
                    left=element.coordinate.left + width * CEll_SIZE
                )
                if (coordinate == searchingCoordinate) {
                    return element
                }
            }
        }
    }
    return null
}