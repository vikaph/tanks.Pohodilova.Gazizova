package com.example.battletanks.enums

import com.example.battletanks.R

enum class Material(
    val tankConGoThrough: Boolean,
val bulletCanGoThrough: Boolean,
val simpleBulletCanDestroy: Boolean,
    val canExistOnlyOne: Boolean,
    val width: Int,
    val height: Int,
    val image: Int
    ) {
    EMPTY(true, true,true, false,0,0,0),
    BRICK(false, false, true,false,1,1, R.drawable.brick),
    CONCRETE(false, false, false, false,1,1,R.drawable.concrete),
    GRASS(true, true, false,false,1,1,R.drawable.grass),
    EAGLE (false, false, true,true,4,3,R.drawable.eagle),
}