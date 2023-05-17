package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: RepositoryAuthInterface,
    private val dataRepository: RepositoryDataInterface
) :
    ViewModel() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableSharedFlow<Status?>(replay = 1)
    val status: MutableSharedFlow<Status?> = _status

    suspend fun clearUpdate() {
        _status.emit(null)
    }

    fun signOut() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                authRepository.signOutUser().collect { _status.emit(it) }
            }
        }
    }

    fun reAuthenticate(password:String){
        viewModelScope.launch {
            withContext(ioDispatcher) {
                authRepository.reAuthenticate(password).collect { _status.emit(it) }
            }
        }
    }
    fun deleteAccount() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dataRepository.deleteAccountData().collect { _status.emit(it) }
            }
            withContext(ioDispatcher) {
                authRepository.deleteAccount().collect { _status.emit(it) }
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dataRepository.getUserData().collect { _status.emit(it) }
            }
        }
    }
}