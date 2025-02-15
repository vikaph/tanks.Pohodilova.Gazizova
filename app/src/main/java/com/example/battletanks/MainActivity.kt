package com.example.battletanks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.FrameLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.example.battletanks.enums.Direction.UP
import com.example.battletanks.enums.Direction.DOWN
import com.example.battletanks.enums.Direction.LEFT
import com.example.battletanks.enums.Direction.RIGHT
import com.example.battletanks.databinding.ActivityMainBinding
import com.example.battletanks.drawers.BulletDrawer
import com.example.battletanks.drawers.ElementsDrawer
import com.example.battletanks.drawers.GridDrawer
import com.example.battletanks.drawers.TankDrawer
import com.example.battletanks.enums.Direction
import com.example.battletanks.enums.Material
import com.example.battletanks.models.Coordinate

lateinit var binding: ActivityMainBinding

const val CEll_SIZE = 50

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val gridDrawer by lazy {
        GridDrawer(binding.container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(binding.container)
    }

    private val tankDrawer by lazy {
        TankDrawer(binding.container)
    }

    private val buttleDrawer by lazy {
        BulletDrawer(binding.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menu"

        binding.editorClear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY}
        binding.editorBrick.setOnClickListener {elementsDrawer.currentMaterial = Material.BRICK}
        binding.editorConcrete.setOnClickListener {
            elementsDrawer.currentMaterial = Material.CONCRETE
        }
        binding.editorGrass.setOnClickListener {elementsDrawer.currentMaterial = Material.GRASS }
        binding.container.setOnTouchListener{_, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
    }

    private fun switchEditMode(){
        if (editMode) {
            gridDrawer.removeGrid()
            binding.materialsContainer.visibility = INVISIBLE
        }else{
            gridDrawer.drawGrid()
            binding.materialsContainer.visibility = VISIBLE
        }
        editMode = !editMode
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                switchEditMode()
                gridDrawer.drawGrid()
                return true

        }
        else -> super.onOptionsItemSelected(item)
    }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
when (keyCode) {
    KEYCODE_DPAD_UP -> tankDrawer.move(binding.myTank, UP, elementsDrawer.elementsOnContainer)
    KEYCODE_DPAD_DOWN-> tankDrawer.move(binding.myTank, DOWN, elementsDrawer.elementsOnContainer)
    KEYCODE_DPAD_LEFT -> tankDrawer.move(binding.myTank, LEFT, elementsDrawer.elementsOnContainer)
    KEYCODE_DPAD_RIGHT -> tankDrawer.move(binding.myTank, RIGHT, elementsDrawer.elementsOnContainer)
    KEYCODE_SPACE -> BulletDrawer.drawBullet(
        binding.myTank,
        tankDrawer.currentDirection
    )
}
        return super.onKeyDown(keyCode, event)
    }


}

