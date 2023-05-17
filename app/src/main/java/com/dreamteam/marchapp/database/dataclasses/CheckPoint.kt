package com.dreamteam.marchapp.database.dataclasses

data class CheckPoint(
    val id: Int,
    val online: Boolean,
    val name: String,
    val dist: Int,
    val coords: String
)
