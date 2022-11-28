package com.dreamteam.marchapp.logic.validation

class PasswordValidator {

    companion object {
        fun validate(password: String): Boolean {
            return password.length in 8..64
        }
    }
}