package com.dreamteam.marchapp.database.dataclasses

data class Event(
    val event_id: Int,
    val event_name: String,
    val event_database: String,
    val is_started: Int,
)
