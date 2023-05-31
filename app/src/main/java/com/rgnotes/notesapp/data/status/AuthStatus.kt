package com.rgnotes.notesapp.data.status

sealed class AuthStatus : Status() {
    object Initial : AuthStatus()
    object Loading : AuthStatus()
    data class Success<T>(val data: T) : AuthStatus()
    data class SetData<T>(val data: T) : AuthStatus()
    data class GetData<T>(val data: T) : AuthStatus()
    data class ReAuthenticate<T>(val data: T) : AuthStatus()
    data class Error(val message: String?) : AuthStatus()
}
