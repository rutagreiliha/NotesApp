package com.rgnotes.notesapp.data.firebase

import com.rgnotes.notesapp.data.Status
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthInterface {
    suspend fun registerUser(email: String, password: String): Flow<Status>
    suspend fun signInUser(email: String, password: String): Flow<Status>
    suspend fun resetPassword(email: String): Flow<Status>
    suspend fun deleteAccount(): Flow<Status>
    suspend fun signOutUser(): Flow<Status>
    suspend fun getUser(): Flow<Status>
}