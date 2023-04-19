package com.rgnotes.notesapp.data.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rgnotes.notesapp.data.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthImplemented : FirebaseAuthInterface {
    override suspend fun registerUser(email: String, password: String): Flow<Status> = flow {
        try {
            val user = Firebase.auth.createUserWithEmailAndPassword(email, password).await().user
            if(user != null){emit(Status.Success(user))}

        } catch (e: Exception) {
            emit(Status.Error(e.toString()))
        }

    }

    override suspend fun signInUser(email: String, password: String): Flow<Status> = flow {
        try {
            val user = Firebase.auth.signInWithEmailAndPassword(email, password).await().user
            if(user != null) { emit(Status.Success(user)) }

        } catch (e: Exception) {
            emit(Status.Error(e.toString()))
        }
    }

    override suspend fun resetPassword(email: String): Flow<Status> = flow {
        try {
            Firebase.auth.sendPasswordResetEmail(email).await().let { emit(Status.Success("Check your email!")) }

        } catch (e: Exception) {
            emit(Status.Error(e.toString()))
        }
    }

    override suspend fun deleteAccount(): Flow<Status> {
        TODO("Not yet implemented")
    }

    override suspend fun signOutUser(): Flow<Status> = flow {
        try {
            Firebase.auth.signOut()
            val user = Firebase.auth.currentUser
            if(user == null){ emit(Status.Success("Success!")) }

        } catch (e: Exception) {
            emit(Status.Error(e.toString()))
        }
    }

    override suspend fun getUser(): Flow<Status> = flow {
        try {
            val user = Firebase.auth.currentUser
            if(user != null){ emit(Status.Success(user)) }

        } catch (e: Exception) {
            emit(Status.Error(e.toString()))
        }
    }
}