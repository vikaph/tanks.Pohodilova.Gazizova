package com.example.battletanks.drawers

import android.app.Activity
import android.view.View
import com.example.battletanks.enums.Direction
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.battletanks.CEll_SIZE
import com.example.battletanks.R
import com.example.battletanks.models.Coordinate
import com.example.battletanks.utils.checkViewCanMoveThrounghBorder

private const val BULLET_WIDTH=15
private const val BULLET_HEIGHT=25

class BulletDrawer(val container: FrameLayout) {
    private var canBulletGoFurther = true
    private var bulletThread: Thread? = null

    private fun checkBulletThreadlive() = bulletThread != null && bulletThread!!.isAlive
    fun makeBulletMove(myTank: View, currentDirection: Direction){
        canBulletGoFurther = true
        if (!checkBulletThreadlive()) {
            bulletThread = Thread(Runnable {
                val bullet = createBullet(myTank, currentDirection)
                while (bullet.checkViewCanMoveThrounghBorder(Coordinate(bullet.top, bullet.left))){
                    when (currentDirection) {
                        Direction.UP -> (bullet.layoutParams as FrameLayout.LayoutParams).topMargin -= BULLET_HEIGHT
                        Direction.DOWN -> (bullet.layoutParams as FrameLayout.LayoutParams).topMargin += BULLET_HEIGHT
                        Direction.LEFT -> (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin -= BULLET_HEIGHT
                        Direction.RIGHT -> (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin += BULLET_HEIGHT
                    }
                    Thread.sleep(30)
                    (container.context as Activity).runOnUiThread{
                        container.removeView(bullet)
                        container.addView(bullet)
                    }
                }
                (container.context as Activity).runOnUiThread {
                    container.removeView(bullet)
                }
                })
            bulletThread!!.start()
        }
    }

    private fun getCoordinateForTopOrBottomDirection(bulletCoordinate: Coordinate):List<Coordinate>{
        val leftCell = bulletCoordinate.left - bulletCoordinate.left% CEll_SIZE
        val rightCell = leftCell + CEll_SIZE
        val topCoordinate = bulletCoordinate.top - bulletCoordinate.top% CEll_SIZE
        return listOf(
            Coordinate(topCoordinate, leftCell),
            Coordinate(topCoordinate, rightCell)
        )
    }

    private fun getCoordinatesForLeftOrRightDirection (bulletCoordinate: Coordinate): List<Coordinate>{
        val topCell = bulletCoordinate.top - bulletCoordinate.top% CEll_SIZE
        val bottomCell = topCell + CEll_SIZE
        val leftCoordinate = bulletCoordinate.left - bulletCoordinate.left% CEll_SIZE
        return listOf(
            Coordinate(topCell, leftCoordinate),
            Coordinate(bottomCell,leftCoordinate)
        )
    }

    private fun createBullet(myTank: View, currentDirection: Direction): ImageView {
        return ImageView(container.context)
            .apply {
                this.setImageResource(R.drawable.bullet)
                this.layoutParams=FrameLayout.LayoutParams(BULLET_WIDTH, BULLET_HEIGHT)
                val bulletCoordinate = getBulletCoordinates(this,myTank,currentDirection)
                (this.layoutParams as FrameLayout.LayoutParams).topMargin=bulletCoordinate.top
                (this.layoutParams as FrameLayout.LayoutParams).leftMargin=bulletCoordinate.left
                this.rotation=currentDirection.rotation
            }
    }

    private fun getBulletCoordinates(
        bullet: ImageView,
        myTank: View,
        currentDirection: Direction
    ): Coordinate {
        val tankLeftTopCoordinate=Coordinate(myTank.top, myTank.left)
       return when (currentDirection) {
            Direction.UP -> Coordinate(
                    top = tankLeftTopCoordinate.top - bullet.layoutParams.height,
                    left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))

            Direction.DOWN -> Coordinate(
                    top = tankLeftTopCoordinate.top + myTank.layoutParams.height,
                    left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))

            Direction.LEFT -> Coordinate(
                    top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top, bullet.layoutParams.height),
                    left = tankLeftTopCoordinate.left - bullet.layoutParams.width)

            Direction.RIGHT -> Coordinate(
                    top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top, bullet.layoutParams.height),
                    left =  tankLeftTopCoordinate.left + myTank.layoutParams.width)
        }
    }

    private fun getDistanceToMiddleOfTank(startCoordinate: Int, bulletSize: Int): Int {
        return startCoordinate + (CEll_SIZE - bulletSize/2)
    }
}