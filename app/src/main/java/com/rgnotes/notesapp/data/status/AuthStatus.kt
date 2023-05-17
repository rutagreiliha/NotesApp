package com.rgnotes.notesapp.data.status

sealed class AuthStatus : Status() {
    class Success<T>(val data: T) : AuthStatus()
    class SetData<T>(val data: T) : AuthStatus()
    class GetData<T>(val data: T) : AuthStatus()
    class ReAuthenticate<T>(val data: T) : AuthStatus()
    class Error(val message: String?) : AuthStatus()
}
