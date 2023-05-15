package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.Note
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val repository: RepositoryDataInterface) :
    ViewModel() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableSharedFlow<DataStatus?>(replay = 1)
    val status: MutableSharedFlow<DataStatus?> = _status

    suspend fun clearUpdate() {
        _status.emit(null)
    }

    fun readNote(id: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.readNote(id).collect { _status.emit(it) }
            }
        }
    }

    fun createNote(note: Note) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.createNote(note).collect { _status.emit(it) }
            }
        }
    }

    fun updateNote(id: String, note: Note) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.updateNote(id, note).collect { _status.emit(it) }
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                repository.deleteNote(id).collect { _status.emit(it) }
            }
        }
    }
}