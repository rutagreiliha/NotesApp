package com.rgnotes.notesapp.data.repo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rgnotes.notesapp.data.firebase.FirebaseInfo.Companion.DATABASE_LOCATION
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.utils.Note
import com.rgnotes.notesapp.data.utils.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RepositoryDataImplemented : RepositoryDataInterface {
    private val database = Firebase.database(DATABASE_LOCATION).reference
    override suspend fun createNote(note: Note): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            val reference =
                database.child("users").child(Firebase.auth.currentUser!!.uid).child("notes").push()
            val key = reference.key
            if (key != null) {
                reference.setValue(note).await()
                reference.child("id").setValue(key).await()
                emit(DataStatus.SetNote("New note added!"))
            }
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun readNote(id: String): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            val note = database.child("users").child(Firebase.auth.currentUser!!.uid).child("notes")
                .child(id).get().await().getValue<Note>()
            emit(DataStatus.GetNote(note))
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun readAllNotes(): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            val listOfNotes = ArrayList<Note>()
            database.child("users").child(Firebase.auth.currentUser!!.uid).child("notes").get()
                .await().children.forEach { item ->
                    listOfNotes.add(
                        Note(
                            item.child("title").value.toString(),
                            item.child("body").value.toString(),
                            item.child("dateTime").value.toString(),
                            item.child("id").value.toString()
                        )
                    )
                }
            emit(DataStatus.GetNote(listOfNotes))
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun updateNote(id: String, note: Note): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            database.child("users").child(Firebase.auth.currentUser!!.uid).child("notes").child(id)
                .setValue(note).await()
            emit(DataStatus.SetNote("Note has been updated!"))
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun deleteNote(
        id: String
    ): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            database.child("users").child(Firebase.auth.currentUser!!.uid).child("notes").child(id)
                .removeValue().await()
            emit(DataStatus.DeleteNote("Note has been deleted!"))
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun deleteAccountData(): Flow<DataStatus> = flow {
        try {
            emit(DataStatus.Loading)
            database.child("users").child(Firebase.auth.currentUser!!.uid).removeValue()
            emit(DataStatus.DeleteNote("Data has been deleted!"))
        } catch (e: Exception) {
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun setUserData(user: User): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            database.child("users").child(Firebase.auth.currentUser!!.uid).child("data")
                .setValue(user).await()
            emit(AuthStatus.SetData("User data set!"))
        } catch (e: Exception) {
            emit(AuthStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun getUserData(): Flow<AuthStatus> = flow {
        try {
            emit(AuthStatus.Loading)
            val userdata =
                database.child("users").child(Firebase.auth.currentUser!!.uid).child("data").get()
                    .await().getValue<User>()
            emit(AuthStatus.GetData(userdata))
        } catch (e: Exception) {
            emit(AuthStatus.Error("Something went wrong!"))
        }
    }
}