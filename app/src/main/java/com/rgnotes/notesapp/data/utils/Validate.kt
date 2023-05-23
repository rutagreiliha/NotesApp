package com.rgnotes.notesapp.data.utils


class Validate {
    companion object{
        fun email(email:String?): Boolean {
            if (email.isNullOrEmpty()) {
                return false
            } else if ("@" !in email || "." !in email) {
                return false
            } else if (email.count() < 4) {
                return false
            } else if ("\n" in email) {
                return false
            }else (return true)
        }

        fun password(password: String?): Boolean {
            if (password.isNullOrEmpty()) {
                return false
            } else if ("\n" in password) {
                return false
            }else if (password.count() < 6) {
                return false
            } else (return true)
        }
    }
}