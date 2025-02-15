package com.example.battletanks.drawers

import android.view.View
import android.widget.FrameLayout
import com.example.battletanks.CEll_SIZE
import com.example.battletanks.binding
import com.example.battletanks.enums.Direction
import com.example.battletanks.models.Coordinate

class TankDrawer(val container: FrameLayout) {
    var currentDirection=Direction.UP
    fun move(myTank: View, direction: Direction, elementsOnContainer: List<Element>){
        val layoutParams = myTank.layoutParams as FrameLayout.LayoutParams
        val currentCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        currentDirection=direction
        when(direction) {
            Direction.UP -> {
                myTank.rotation = 0f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += -CEll_SIZE
            }
            Direction.DOWN -> {
                myTank.rotation = 180f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += CEll_SIZE
            }
            Direction.LEFT -> {
                myTank.rotation = 270f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin -= CEll_SIZE
            }
            Direction.RIGHT -> {
                myTank.rotation = 90f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin += CEll_SIZE
            }
        }

        val nextCoordinate = Coordinate(layoutParams.topMargin,layoutParams.leftMargin)
        if (checkTankCanMoveThrounghBorder(
                nextCoordinate,
                myTank
            ) && checkTankCanMoveThrounghMaterial(nextCoordinate, elementsOnContainer)
        ){
            binding.container.removeView(myTank)
            binding.container.addView(myTank,0)
        } else {
            (myTank.layoutParams as FrameLayout.LayoutParams).topMargin=currentCoordinate.top
            (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin=currentCoordinate.left
        }
    }

    private fun checkTankCanMoveThrounghMaterial(
        coordinate: Coordinate,
        elementsOnContainer: List<Element>
    ): Boolean {
        getTankCoordinates(coordinate).forEach {
            val element = getElementByCoordinates(it, elementsOnContainer)
            if (element != null && !element.material.tankConGoThrough){
                return false
            }
        }
        return true
    }

    private fun checkTankCanMoveThrounghBorder(coordinate: Coordinate, myTank: View): Boolean {
        return coordinate.top >= 0 &&
                coordinate.top+ myTank.height <= binding.container.height &&
                coordinate.left >= 0 &&
                coordinate.left+ myTank.width <= binding.container.width
    }

    private fun getTankCoordinates(topLeftCoordinate: Coordinate): List<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        coordinateList.add(topLeftCoordinate)
        coordinateList.add(Coordinate(topLeftCoordinate.top + CEll_SIZE, topLeftCoordinate.left))
        coordinateList.add(Coordinate(topLeftCoordinate.top, topLeftCoordinate.left + CEll_SIZE))
        coordinateList.add(
            Coordinate(
                topLeftCoordinate.top + CEll_SIZE,
                topLeftCoordinate.left + CEll_SIZE
            )
        )
        return coordinateList
    }

    private fun getElementByCoordinates(
        coordinate: Coordinate, elementsOnContainer: List<Element>
    )=
        elementsOnContainer.firstOrNull {it.coordinate == coordinate}
}