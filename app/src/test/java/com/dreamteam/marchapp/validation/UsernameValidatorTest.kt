package com.dreamteam.marchapp.validation

import com.dreamteam.marchapp.logic.validation.UsernameValidator
import org.junit.Assert.*
import org.junit.Test

class UsernameValidatorTest {

    @Test
    fun `returns true when username is correct`() {
        assertTrue(UsernameValidator.validate("username"))
        assertTrue(UsernameValidator.validate("adam123"))
        assertTrue(UsernameValidator.validate("olanowak12"))
        assertTrue(UsernameValidator.validate("marchuser44"))
        assertTrue(UsernameValidator.validate("jan2137"))
    }

    @Test
    fun `returns false when username is incorrect`() {
        assertFalse(UsernameValidator.validate("username_+!!"))
        assertFalse(UsernameValidator.validate(""))
        assertFalse(UsernameValidator.validate("xD"))
        assertFalse(UsernameValidator.validate("123"))
        assertFalse(UsernameValidator.validate("!?????????"))
        assertFalse(UsernameValidator.validate("uzytko?nik"))
    }
}