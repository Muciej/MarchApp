package com.dreamteam.marchapp.database.dataclasses

data class Participant(
    val accId: Int,
    val startNumber: Int,
    var name: String,
    val surname: String,
    val nickname: String,
    val qrCodeData: String,
)
