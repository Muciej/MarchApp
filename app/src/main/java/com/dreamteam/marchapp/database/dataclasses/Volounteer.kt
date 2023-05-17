package com.dreamteam.marchapp.database.dataclasses

data class Volounteer(
    val accountId: Int,
    val staffId: Int,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val mail: String,
    val login: String,
) {
    var pointId: Int? = null
}