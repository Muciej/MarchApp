package com.dreamteam.marchapp.validation

import com.dreamteam.marchapp.logic.validation.PasswordValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun `returns true for correct password`() {
        assertTrue(PasswordValidator.validate("trudneHaslo123"))
        assertTrue(PasswordValidator.validate("werfwrotjwpot3fewij0"))
        assertTrue(PasswordValidator.validate("EQW)243RR2=+E32DEDfde"))
        assertTrue(PasswordValidator.validate("E_D-_______32xdxxdxd222fde"))
    }

    @Test
    fun `returns false for incorrect password`() {
        assertFalse(PasswordValidator.validate("2137111"))
        assertFalse(PasswordValidator.validate("krotkie"))
        assertFalse(PasswordValidator.validate(""))
        assertFalse(PasswordValidator.validate("EQW)243RR2=+E32DEDfdeE32DEDfdeE32DEDfdeE32DEDfdeE32DEDfdeE32DEDfdeE32DEDfdeE32DEDfde"))
        assertFalse(PasswordValidator.validate("dlugiedlugiedlugiedlugiedlugiedlugiedlugiedlugiedlugiedlugiedlugiedlugie"))
    }
}