package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: RepositoryAuthInterface,
    private val dataRepository: RepositoryDataInterface,
    private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {


    private val _status = MutableStateFlow<Status?>(AuthStatus.Initial)
    val status = _status.asStateFlow()

    suspend fun clearUpdate() {
        _status.value = null
    }

    fun signOut() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                authRepository.signOutUser().collect { _status.value = it }
            }
        }
    }

    fun reAuthenticate(password: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                authRepository.reAuthenticate(password).collect { _status.value = it }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dataRepository.deleteAccountData().collect { _status.value = it }
            }
            withContext(ioDispatcher) {
                authRepository.deleteAccount().collect { _status.value = it }
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dataRepository.getUserData().collect { _status.value = it }
            }
        }
    }
}