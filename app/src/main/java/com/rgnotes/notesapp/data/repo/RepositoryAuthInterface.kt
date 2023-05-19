package com.rgnotes.notesapp.data.repo

import com.rgnotes.notesapp.data.status.AuthStatus
import kotlinx.coroutines.flow.Flow

interface RepositoryAuthInterface {
    suspend fun registerUser(email: String, password: String): Flow<AuthStatus>
    suspend fun signInUser(email: String, password: String): Flow<AuthStatus>
    suspend fun resetPassword(email: String): Flow<AuthStatus>
    suspend fun deleteAccount(): Flow<AuthStatus>
    suspend fun signOutUser(): Flow<AuthStatus>
    suspend fun isUserSignedIn(): Flow<AuthStatus>
    suspend fun reAuthenticate(password: String): Flow<AuthStatus>
}