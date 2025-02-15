package com.example.battletanks.drawers

import android.view.View
import com.example.battletanks.enums.Direction
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.battletanks.R
import com.example.battletanks.models.Coordinate

private const val BULLET_WIDTH=15
private const val BULLET_HEIGHT=15

class BulletDrawer(val container: FrameLayout) {
    fun drawBullet (myTank: View, currentDirection: Direction){
        val bullet = ImageView(container.context)
            .apply {
                this.setImageResource(R.drawable.bullet)
                this.layoutParams=FrameLayout.LayoutParams(BULLET_WIDTH, BULLET_HEIGHT)
                val bulletCoordinate = getBulletCoordinates(this,myTank,currentDirection)
                (this.layoutParams as FrameLayout.LayoutParams).topMargin=bulletCoordinate.top
                (this.layoutParams as FrameLayout.LayoutParams).leftMargin=bulletCoordinate.left
            }
        container.addView(bullet)
    }

    private fun getBulletCoordinates(
        bullet: ImageView,
        myTank: View,
        currentDirection: Direction
    ): Coordinate {
        val tankLeftTopCoordinate=Coordinate(myTank.top, myTank.left)
        return tankLeftTopCoordinate

    }
}