package com.rgnotes.notesapp.data.repo

import com.rgnotes.notesapp.data.firebase.FirebaseAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryAuthImplemented @Inject constructor(private val firebaseAuth: FirebaseAuthInterface) :
    RepositoryAuthInterface {
    override suspend fun registerUser(email: String, password: String): Flow<AuthStatus> {
        return firebaseAuth.registerUser(email, password)
    }

    override suspend fun signInUser(email: String, password: String): Flow<AuthStatus> {
        return firebaseAuth.signInUser(email, password)
    }

    override suspend fun resetPassword(email: String): Flow<AuthStatus> {
        return firebaseAuth.resetPassword(email)
    }

    override suspend fun deleteAccount(): Flow<AuthStatus> {
        return firebaseAuth.deleteAccount()
    }

    override suspend fun signOutUser(): Flow<AuthStatus> {
        return firebaseAuth.signOutUser()
    }

    override suspend fun isUserSignedIn(): Flow<AuthStatus> {
        return firebaseAuth.isUserSignedIn()
    }
    override suspend fun reAuthenticate(password: String): Flow<AuthStatus> {
        return firebaseAuth.reAuthenticate(password)
    }
}