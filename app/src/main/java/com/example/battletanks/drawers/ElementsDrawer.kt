package com.example.battletanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.battletanks.CEll_SIZE
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
         drawView(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial) {
            replaceView(coordinate)
        }
    }

    fun drawElementsList(elements: List<Element>?) {
        if (elements ==null){
            return
        }
        for (element in elements) {
            currentMaterial=element.material
         drawView((element.coordinate))
        }
    }

    private fun replaceView(coordinate: Coordinate) {
        eraseView(coordinate)
        drawView(coordinate)
    }

    private fun eraseView(coordinate: Coordinate) {
        removeElement(getElementByCoordinates(coordinate, elementsOnContainer))
        for (element in getElementsUnderCurrentCoordinate(coordinate)){
            removeElement(element)
        }
    }

    private fun removeElement(element: Element?){
        if (element !=null) {
            val erasingView = container.findViewById<View>(element.viewId)
            container.removeView(erasingView)
            elementsOnContainer.remove(element)
        }
    }

    private fun getElementsUnderCurrentCoordinate(coordinate: Coordinate): List<Element> {
        val elements = mutableListOf<Element>()
        for (element in elementsOnContainer) {
            for (height in 0 until currentMaterial.height) {
                for (width in 0 until currentMaterial.width) {
                    if (element.coordinate == Coordinate(
                            coordinate.top +height* CEll_SIZE,
                            coordinate.left+width* CEll_SIZE
                    )
                    ){
                        elements.add(element)
                    }
                }
            }
        }
        return elements
    }


    private fun removeIfSingleInstance() {
        if (currentMaterial.canExistOnlyOne) {
            elementsOnContainer.firstOrNull { it.material == currentMaterial}?.coordinate?.let {
                eraseView(it)
            }
        }
    }

    private fun drawView(coordinate: Coordinate) {
        removeIfSingleInstance()
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(
            currentMaterial.width*CEll_SIZE,
            currentMaterial.height*CEll_SIZE)
        view.setImageResource(currentMaterial.image)
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val element = Element(
            material=currentMaterial,
            coordinate = coordinate,
            width = currentMaterial.width,
            height = currentMaterial.height
        )
        view.id = element.viewId
        view.layoutParams = layoutParams
        view.scaleType = ImageView.ScaleType.FIT_XY
        container.addView(view)
        elementsOnContainer.add(element)
    }
}

