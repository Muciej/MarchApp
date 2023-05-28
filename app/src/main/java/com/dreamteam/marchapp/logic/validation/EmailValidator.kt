package com.dreamteam.marchapp.logic.validation

import androidx.core.util.PatternsCompat

class EmailValidator {

    companion object {
        fun validate(email: String): Boolean {
            return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}