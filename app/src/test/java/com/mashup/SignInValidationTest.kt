package com.mashup

import com.mashup.ui.signup.model.Validation
import com.mashup.ui.signup.validationId
import com.mashup.ui.signup.validationName
import com.mashup.ui.signup.validationPlatform
import com.mashup.ui.signup.validationPwd
import junit.framework.Assert.assertEquals
import org.junit.Test

class SignInValidationTest {

    @Test
    fun validation_id_only_number() {
        val id = "0103"
        assertEquals(validationId(id), Validation.FAILED)
    }

    @Test
    fun validation_id_number_or_character() {
        val id = "0103a"
        assertEquals(validationId(id), Validation.FAILED)
    }

    @Test
    fun validation_id_only_character() {
        val id = "abce"
        assertEquals(validationId(id), Validation.FAILED)
    }

    @Test
    fun validation_pwd_only_number() {
        val pwd = "0103"
        assertEquals(validationPwd(pwd), Validation.FAILED)
    }

    @Test
    fun validation_pwd_number_or_character_less_than_8() {
        val pwd = "0103a"
        assertEquals(validationPwd(pwd), Validation.FAILED)
    }

    @Test
    fun validation_pwd_number_or_character_great_than_8() {
        val id = "abce"
        assertEquals(validationId(id), Validation.SUCCESS)
    }

    @Test
    fun validation_name_empty() {
        val name = ""
        assertEquals(validationName(name), Validation.EMPTY)
    }

    @Test
    fun validation_name_not_empty() {
        val name = "name"
        assertEquals(validationName(name), Validation.SUCCESS)
    }

    @Test
    fun validation_name_platform() {
        val platform = ""
        assertEquals(validationPlatform(platform), Validation.EMPTY)
    }

    @Test
    fun validation_name_not_platform() {
        val platform = "platform"
        assertEquals(validationPlatform(platform), Validation.SUCCESS)
    }
}