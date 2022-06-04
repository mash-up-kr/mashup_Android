package com.mashup

import com.mashup.ui.signup.validationId
import com.mashup.ui.signup.validationPwd
import junit.framework.Assert.assertEquals
import org.junit.Test

class SignInValidationTest {

    @Test
    fun validation_id_only_number() {
        val id = "0103"
        assertEquals(validationId(id), false)
    }

    @Test
    fun validation_id_number_or_character() {
        val id = "0103a"
        assertEquals(validationId(id), false)
    }

    @Test
    fun validation_id_only_character() {
        val id = "abce"
        assertEquals(validationId(id), true)
    }

    @Test
    fun validation_pwd_only_number() {
        val pwd = "0103"
        assertEquals(validationPwd(pwd), false)
    }

    @Test
    fun validation_pwd_number_or_character_less_than_8() {
        val pwd = "0103a"
        assertEquals(validationPwd(pwd), false)
    }

    @Test
    fun validation_pwd_number_or_character_great_than_8() {
        val id = "abce"
        assertEquals(validationId(id), true)
    }
}