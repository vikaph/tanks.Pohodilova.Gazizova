package com.example.battletanks.drawers

import android.app.ActionBar.LayoutParams
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.example.battletanks.CEll_SIZE
import com.example.battletanks.R
import com.example.battletanks.binding
import com.example.battletanks.enums.Direction
import com.example.battletanks.enums.Material
import com.example.battletanks.models.Coordinate
import com.example.battletanks.models.Element
import com.example.battletanks.utils.getElementByCoordinates

const val CELLS_SIMPLE_ELEMENT =1
const val CELLS_EAGLE_WIDTH=4
const val CELLS_EAGLE_HEIGHT=3

class ElementsDrawer (val container: FrameLayout) {
    var currentMaterial = Material.EMPTY
    val elementsOnContainer = mutableListOf<Element>()

    fun onTouchContainer(x: Float, y: Float) {
        val topMargin = y.toInt() - (y.toInt() % CEll_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CEll_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (currentMaterial == Material.EMPTY) {
            eraseView(coordinate)
        } else {
            drawOrReplaceView(coordinate)
        }
    }


    private fun drawOrReplaceView(coordinate: Coordinate) {
        val viewOnCoordinate = getElementByCoordinates(coordinate,elementsOnContainer)
        if (viewOnCoordinate == null) {
            selectMaterial(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial) {
            replaceView(coordinate)
        }
    }

    private fun replaceView(coordinate: Coordinate) {
        eraseView(coordinate)
        selectMaterial(coordinate)
    }

    private fun eraseView(coordinate: Coordinate) {
        val elementOnCoordinate = getElementByCoordinates(coordinate, elementsOnContainer)
        if (elementOnCoordinate != null) {
            val erasingView = container.findViewById<View>(elementOnCoordinate.viewId)
            container.removeView(erasingView)
            elementsOnContainer.remove(elementOnCoordinate)
        }
    }


    fun selectMaterial(coordinate: Coordinate) {
        when (currentMaterial) {
            Material.BRICK -> drawView(R.drawable.brick, coordinate)
            Material.CONCRETE -> drawView(R.drawable.concrete, coordinate)
            Material.GRASS -> drawView(R.drawable.grass, coordinate)
            Material.EAGLE -> {
                removeExistingEagle()
                drawView(R.drawable.eagle, coordinate, CELLS_EAGLE_WIDTH, CELLS_EAGLE_HEIGHT)
            }
            Material.EMPTY -> {}
        }
    }

    private fun removeExistingEagle(){
        elementsOnContainer.firstOrNull {it.material == Material.EAGLE}?.coordinate?.let {
            eraseView(it)
        }
    }

    private fun drawView(
        @DrawableRes image: Int,
        coordinate: Coordinate,
        width: Int = CELLS_SIMPLE_ELEMENT,
        height: Int= CELLS_SIMPLE_ELEMENT
    ) {
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(width*CEll_SIZE, height*CEll_SIZE)
        view.setImageResource(image)
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val viewId = View.generateViewId()
        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementsOnContainer.add(Element(viewId, currentMaterial, coordinate, width, height))
    }

}

