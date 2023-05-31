package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import com.rgnotes.notesapp.data.utils.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: RepositoryAuthInterface,
    private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val _status = MutableStateFlow<Status?>(AuthStatus.Initial())
    val status = _status.asStateFlow()

    fun clearUpdate() {
        _status.value = null
    }

    fun signInUser(email: String?, password: String?) {
        viewModelScope.launch {
            if (Validate.email(email)) {
                if (Validate.password(password)) {
                    withContext(ioDispatcher) {
                        authRepo.signInUser(email!!, password!!).collect { _status.value = it }
                    }
                } else {
                    _status.value = AuthStatus.Error("Invalid password format!")
                }
            } else {
                _status.value = AuthStatus.Error("Invalid email format!")
            }
        }
    }

}