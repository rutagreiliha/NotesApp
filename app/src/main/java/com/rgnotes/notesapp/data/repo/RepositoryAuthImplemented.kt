package com.rgnotes.notesapp.data.repo

import com.rgnotes.notesapp.data.Status
import com.rgnotes.notesapp.data.firebase.FirebaseAuthInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryAuthImplemented @Inject constructor(private val firebaseAuth: FirebaseAuthInterface) : RepositoryAuthInterface {
    override suspend fun registerUser(email: String, password: String): Flow<Status> {
        return firebaseAuth.registerUser(email, password)
    }

    override suspend fun signInUser(email: String, password: String): Flow<Status> {
        return firebaseAuth.signInUser(email, password)
    }

    override suspend fun resetPassword(email: String): Flow<Status> {
        return firebaseAuth.resetPassword(email)
    }

    override suspend fun deleteAccount(): Flow<Status> {
        return firebaseAuth.deleteAccount()
    }

    override suspend fun signOutUser(): Flow<Status> {
        return firebaseAuth.signOutUser()
    }

    override suspend fun getUser(): Flow<Status> {
        return firebaseAuth.getUser()
    }
}