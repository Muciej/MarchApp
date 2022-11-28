package com.dreamteam.marchapp.logic.validation

import android.util.Patterns

class EmailValidator {

    companion object {
        fun validate(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}