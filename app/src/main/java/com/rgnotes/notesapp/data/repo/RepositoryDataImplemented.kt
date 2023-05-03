package com.rgnotes.notesapp.data.repo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rgnotes.notesapp.data.Note
import com.rgnotes.notesapp.data.NoteTitleId
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.firebase.FirebaseInfo.Companion.DATABASE_LOCATION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RepositoryDataImplemented : RepositoryDataInterface{
    private val database = Firebase.database(DATABASE_LOCATION).reference
    override suspend fun createNote(note: Note): Flow<DataStatus> =flow{
        try{
            val reference = database.child("users").child( Firebase.auth.currentUser!!.uid).child("notes").push()
            val key = reference.key
            if(key != null){
                reference.setValue(note).await()
                database.child("users").child( Firebase.auth.currentUser!!.uid).child("titles").child(key).setValue(note.title).await()
                emit(DataStatus.SetNote("New note added!"))
            }
        }catch (e:Exception){
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun readNote(id: String): Flow<DataStatus> =flow {
        try{
            val note = database.child("users").child( Firebase.auth.currentUser!!.uid).child("notes").child(id).get().await().getValue<Note>()
            emit(DataStatus.GetNote(note))
        }catch (e:Exception){
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun readAllNotes(): Flow<DataStatus> =flow {
        try{
            val listOfNotes = ArrayList<NoteTitleId>()
            val note = database.child("users").child( Firebase.auth.currentUser!!.uid).child("titles").get().await().children.forEach { item -> listOfNotes.add(
                NoteTitleId(item.key, item.value as String?)
            ) }
            emit(DataStatus.GetNote(listOfNotes))
        }catch (e:Exception){
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun updateNote(id: String, note: Note): Flow<DataStatus> =flow {
        try{
            database.child("users").child( Firebase.auth.currentUser!!.uid).child("titles").child(id).setValue(note.title).await()
            database.child("users").child( Firebase.auth.currentUser!!.uid).child("notes").child(id).setValue(note).await()
            emit(DataStatus.SetNote("Note has been updated!"))

        }catch (e:Exception){
            emit(DataStatus.Error("Something went wrong!"))
        }
    }

    override suspend fun deleteNote(
        id: String
    ): Flow<DataStatus> =flow {
        try{
            database.child("users").child( Firebase.auth.currentUser!!.uid).child("titles").child(id).removeValue().await()
            database.child("users").child( Firebase.auth.currentUser!!.uid).child("notes").child(id).removeValue().await()
            emit(DataStatus.DeleteNote("Note has been deleted!"))

        }catch (e:Exception){
            emit(DataStatus.Error("Something went wrong!"))
        }
    }
}