package com.dreamteam.marchapp.validation

import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.NameValidator
import org.junit.Assert
import org.junit.Test

class NameAndLastNameValidatorTest {

    @Test
    fun `returns true when Name is correct`() {
        Assert.assertTrue(NameValidator.validate("Tomasz"))
        Assert.assertTrue(NameValidator.validate("Piotr"))
        Assert.assertTrue(NameValidator.validate("Maciej"))
    }

    @Test
    fun `returns false when Name is incorrect`() {
        Assert.assertFalse(NameValidator.validate("Bartosz_102"))
        Assert.assertFalse(NameValidator.validate("aneczka   ann"))
        Assert.assertFalse(NameValidator.validate("XDMATEUSZ"))
    }


    @Test
    fun `returns true when LastName is correct`() {
        Assert.assertTrue(LastNameValidator.validate("Hołub"))
        Assert.assertTrue(LastNameValidator.validate("Kowalski"))
    }

    @Test
    fun `returns false when LastName is incorrect`() {
        Assert.assertFalse(LastNameValidator.validate("adamczyk"))
        Assert.assertFalse(LastNameValidator.validate("Grelewski 123"))
        Assert.assertFalse(LastNameValidator.validate("Piotro!wski_!"))
    }
}