package com.dreamteam.marchapp.logic.validation

class UsernameValidator {

    companion object {
        fun validate(username: String): Boolean {
            return username.length in 5..15 && username.all { it.isLetterOrDigit() }
        }
    }
}