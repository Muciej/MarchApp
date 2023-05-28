package com.dreamteam.marchapp.validation

import com.dreamteam.marchapp.logic.validation.EmailValidator
import org.junit.Assert.*
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun `returns true when email is correct`() {
        assertTrue(EmailValidator.validate("test@gmail.com"))
        assertTrue(EmailValidator.validate("adam@onet.pl"))
        assertTrue(EmailValidator.validate("joanna.kowalska@wp.pl"))
    }

    @Test
    fun `returns false when email is incorrect`() {
        assertFalse(EmailValidator.validate("@wp.pl"))
        assertFalse(EmailValidator.validate("adam.pl"))
        assertFalse(EmailValidator.validate("onet@pl"))
        assertFalse(EmailValidator.validate(""))
        assertFalse(EmailValidator.validate("123"))
        assertFalse(EmailValidator.validate("313413"))
    }
}