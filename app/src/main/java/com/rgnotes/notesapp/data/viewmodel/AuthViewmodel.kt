package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(private val repository: RepositoryAuthInterface) :
    ViewModel() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableSharedFlow<Status?>(replay = 1)
    val status: MutableSharedFlow<Status?> = _status

    suspend fun clearUpdate() {
        _status.emit(null)
    }

    private suspend fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrEmpty()) {
            _status.emit(DataStatus.Error("Please enter your email!"))
            return false
        } else if (!email.contains('@')&&!email.contains('.')) {
            _status.emit(DataStatus.Error("Invalid format!"))
            return false
        } else if (email.count() < 4) {
            _status.emit(DataStatus.Error("Invalid format!"))
            return false
        } else (return true)
    }

    private suspend fun isPasswordValid(password: String?): Boolean {
        if (password.isNullOrEmpty()) {
            _status.emit(DataStatus.Error("Please enter your password!"))
            return false
        } else if (password.count() < 6) {
            _status.emit(DataStatus.Error("Password must be at least 6 characters!"))
            return false
        } else (return true)
    }

    suspend fun signIn(email: String?, password: String?) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            viewModelScope.launch {
                withContext(ioDispatcher) {
                    repository.signInUser(email!!, password!!).collect { _status.emit(it) }
                }
            }
        }
    }

    suspend fun signUp(email: String?, password: String?) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            viewModelScope.launch {
                withContext(ioDispatcher) {
                    repository.registerUser(email!!, password!!).collect { _status.emit(it) }
                }
            }
        }
    }

    suspend fun resetPassword(email: String?) {
        if (isEmailValid(email)) {
            viewModelScope.launch {
                withContext(ioDispatcher) {
                    repository.resetPassword(email!!).collect { _status.emit(it) }
                }
            }
        }
    }

    suspend fun isUserSignedIn() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.isUserSignedIn().collect { _status.emit(it) }
            }
        }
    }

    suspend fun deleteAccount() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.deleteAccount().collect { _status.emit(it) }
            }
        }
    }
}