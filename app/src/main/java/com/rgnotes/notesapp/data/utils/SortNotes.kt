package com.rgnotes.notesapp.data.utils

object SortNotes {
    val orders = arrayOf("Newest first", "Oldest first", "A to Z", "Z to A")
    fun sortNotes(notes: ArrayList<Note>, order:String ){
        when (order){
            "Newest first" -> notes.sortByDescending { it.dateTime }
            "Oldest first" -> notes.sortBy { it.dateTime }
            "A to Z" -> notes.sortBy { it.title }
            "Z to A" -> notes.sortByDescending { it.title }
        }
    }
}