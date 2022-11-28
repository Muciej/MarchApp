package com.dreamteam.marchapp.logic.validation

class NameValidator {
    companion object {
        fun validate(name: String): Boolean {
            return name[0].isUpperCase() and !name.isNullOrBlank() and
                    name.substring(1, name.length).all { it.isLowerCase() } and !name.contains(" ")
        }
    }
}