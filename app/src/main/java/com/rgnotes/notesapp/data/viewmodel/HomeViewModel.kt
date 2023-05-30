package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: RepositoryAuthInterface,
    private val dataRepo: RepositoryDataInterface
) : ViewModel() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableStateFlow<Status?>(AuthStatus.Initial())
    val status = _status.asStateFlow()

    fun clearUpdate() {
        _status.value =null
    }

    fun isUserSignedIn() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                authRepo.isUserSignedIn().collect { _status.value =it }
            }
        }
    }

    fun readAllNotes() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dataRepo.readAllNotes().collect { _status.value =it }
            }
        }
    }
}