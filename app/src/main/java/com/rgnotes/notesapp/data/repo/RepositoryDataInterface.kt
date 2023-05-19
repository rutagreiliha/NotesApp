package com.rgnotes.notesapp.data.repo

import com.rgnotes.notesapp.data.utils.Note
import com.rgnotes.notesapp.data.utils.User
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.DataStatus
import kotlinx.coroutines.flow.Flow

interface RepositoryDataInterface {
    suspend fun createNote(note: Note): Flow<DataStatus>
    suspend fun readNote(id: String): Flow<DataStatus>
    suspend fun readAllNotes(): Flow<DataStatus>
    suspend fun updateNote(id: String, note: Note): Flow<DataStatus>
    suspend fun deleteNote(id: String): Flow<DataStatus>
    suspend fun deleteAccountData(): Flow<DataStatus>
    suspend fun setUserData(user: User): Flow<AuthStatus>
    suspend fun getUserData(): Flow<AuthStatus>

}