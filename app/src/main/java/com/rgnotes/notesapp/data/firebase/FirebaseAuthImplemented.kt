package com.rgnotes.notesapp.data.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rgnotes.notesapp.data.status.AuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthImplemented : FirebaseAuthInterface {
    override suspend fun registerUser(email: String, password: String): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            val user = Firebase.auth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                emit(AuthStatus.Success(user))
            }
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun signInUser(email: String, password: String): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            val user = Firebase.auth.signInWithEmailAndPassword(email, password).await().user
            if (user != null) {
                emit(AuthStatus.Success(user))
            }
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun resetPassword(email: String): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            Firebase.auth.sendPasswordResetEmail(email).await()
                .let { emit(AuthStatus.Success("Check your email!")) }
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun deleteAccount(): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            Firebase.auth.currentUser!!.delete().await()
            emit(AuthStatus.Success("Success!"))
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun signOutUser(): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            Firebase.auth.signOut()
            val user = Firebase.auth.currentUser
            if (user == null) {
                emit(AuthStatus.Success("Success!"))
            }
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun isUserSignedIn(): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            val user = Firebase.auth.currentUser
            if (user != null) {
                emit(AuthStatus.Success(user))
            } else {
                emit(AuthStatus.Error("User is not signed in"))
            }

        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }

    override suspend fun reAuthenticate(password: String): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            val email = Firebase.auth.currentUser!!.email!!
            val user = Firebase.auth.signInWithEmailAndPassword(email, password).await().user
            if (user != null) {
                emit(AuthStatus.ReAuthenticate(user))
            }
        } catch (e: Exception) {
            emit(AuthStatus.Error(e.toString().split(":")[1]))
        }
    }
}