package com.rgnotes.notesapp.data

sealed class Status {
    class Success<T>(data: T) : Status()
    class Loading : Status()
    class Error(message: String?) : Status()
}