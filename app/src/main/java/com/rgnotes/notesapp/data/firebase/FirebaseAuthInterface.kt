package com.rgnotes.notesapp.data.firebase

import com.rgnotes.notesapp.data.status.AuthStatus
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthInterface {
    suspend fun registerUser(email: String, password: String): Flow<AuthStatus>
    suspend fun signInUser(email: String, password: String): Flow<AuthStatus>
    suspend fun resetPassword(email: String): Flow<AuthStatus>
    suspend fun deleteAccount(): Flow<AuthStatus>
    suspend fun signOutUser(): Flow<AuthStatus>
    suspend fun isUserSignedIn(): Flow<AuthStatus>
    suspend fun reAuthenticate(password: String): Flow<AuthStatus>
}