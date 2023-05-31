package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.status.Status
import com.rgnotes.notesapp.data.utils.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val repository: RepositoryDataInterface,
    private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val _status = MutableStateFlow<Status?>(DataStatus.Initial())
    val status = _status.asStateFlow()

    suspend fun clearUpdate() {
        _status.value = null
    }

    fun readNote(id: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.readNote(id).collect { _status.value = it }
            }
        }
    }

    fun createNote(note: Note) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.createNote(note).collect { _status.value = it }
            }
        }
    }

    fun updateNote(id: String, note: Note) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.updateNote(id, note).collect { _status.value = it }
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.deleteNote(id).collect { _status.value = it }

            }
        }
    }
}