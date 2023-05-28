package com.dreamteam.marchapp.database.dataclasses

data class Account(
    val id: Int,
    val login: String,
    val role: Roles,
)