package com.dreamteam.marchapp.logic.validation

class LastNameValidator {
    companion object{
        fun validate(lastName: String): Boolean {
            return lastName[0].isUpperCase() and !lastName.isNullOrBlank() and
                    lastName.substring(1, lastName.length).all { it.isLowerCase() } and !lastName.contains(" ")
    }

    }
}