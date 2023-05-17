package com.dreamteam.marchapp.logic.validation

class PhoneValidator {

    companion object {
        fun validate(phone: String): Boolean {
            return phone.length == 9 && phone.all { it.isDigit() }
        }
    }
}