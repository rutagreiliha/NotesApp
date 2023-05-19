package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepo: RepositoryAuthInterface) :
    ViewModel() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableSharedFlow<Status?>(replay = 1)
    val status: MutableSharedFlow<Status?> = _status

    suspend fun clearUpdate() {
        _status.emit(null)
    }

    private suspend fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrEmpty()) {
            _status.emit(AuthStatus.Error("Email can't be empty!"))
            return false
        } else if ("@" !in email || "." !in email) {
            _status.emit(AuthStatus.Error("Invalid email format!"))
            return false
        } else if (email.count() < 4) {
            _status.emit(AuthStatus.Error("Invalid email format!"))
            return false
        } else (return true)
    }

    private suspend fun isPasswordValid(password: String?): Boolean {
        if (password.isNullOrEmpty()) {
            _status.emit(AuthStatus.Error("Password can't be empty!"))
            return false
        } else if (password.count() < 6) {
            _status.emit(AuthStatus.Error("Password must be at least 6 characters!"))
            return false
        } else (return true)
    }

    fun signInUser(email: String?, password: String?) {
        viewModelScope.launch {
            if (isEmailValid(email) and isPasswordValid(password)) {
                withContext(ioDispatcher) {
                    authRepo.signInUser(email!!, password!!).collect { _status.emit(it) }
                }
            }
        }
    }
}