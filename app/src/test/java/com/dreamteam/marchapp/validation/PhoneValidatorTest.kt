package com.dreamteam.marchapp.validation

import com.dreamteam.marchapp.logic.validation.PhoneValidator
import org.junit.Assert.*
import org.junit.Test

class PhoneValidatorTest {

    @Test fun `returns true when phone number is correct`() {
        assertTrue(PhoneValidator.validate("515515515"))
        assertTrue(PhoneValidator.validate("123456789"))
        assertTrue(PhoneValidator.validate("508411032"))
        assertTrue(PhoneValidator.validate("413845002"))
    }

    @Test fun `returns false when phone number is incorrect`() {
        assertFalse(PhoneValidator.validate("numer"))
        assertFalse(PhoneValidator.validate(""))
        assertFalse(PhoneValidator.validate("123"))
        assertFalse(PhoneValidator.validate("40135013051308580"))
        assertFalse(PhoneValidator.validate("14412312"))
        assertFalse(PhoneValidator.validate("997"))
    }
}