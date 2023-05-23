package com.rgnotes.notesapp.data.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateTest {
    @Test
    fun validate_email_when_email_correct_format_returns_true() {
        val emails = listOf("abc.def@email.com", "ab@email.com","a@e.c")
        for (email in emails){assertEquals(Validate.email(email),true)}
    }
    @Test
    fun validate_email_when_email_wrong_format_returns_false() {
        val emails = listOf("abc@", "email.com","a@e","",null,"@.","abc.def@h.c\n","\n")
        for (email in emails){assertEquals(Validate.email(email),false)}
    }
    @Test
    fun validate_password_when_password_correct_format_returns_true() {
        val passwords = listOf("Password12", "27738837","123456","###hdo8sk")
        for (password in passwords){assertEquals(Validate.password(password),true)}
    }
    @Test
    fun validate_password_when_password_wrong_format_returns_false() {
        val passwords = listOf("",null, " mnh","12","12345","\nhjdgtfgh5","l##}{")
        for (password in passwords){assertEquals(Validate.password(password),false)}
    }
}