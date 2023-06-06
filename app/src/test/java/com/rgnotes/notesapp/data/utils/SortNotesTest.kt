package com.rgnotes.notesapp.data.utils

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class SortNotesTest {
    private lateinit var notes: ArrayList<Note>
    @Before
    fun setUp(){
        notes = ArrayList()
        notes.add(Note("noteA","",LocalDateTime.now().toString()))
        notes.add(Note("noteB","",LocalDateTime.now().minusMinutes(2).toString()))
    }

    @Test
    fun sort_notes_when_newest_first_returns_the_right_order() {
        SortNotes.sortNotes(notes,"Newest first")
        assertEquals("noteA",notes[0].title)
        assertEquals("noteB",notes[1].title)
    }
    @Test
    fun sort_notes_when_oldest_first_returns_the_right_order() {
        SortNotes.sortNotes(notes,"Oldest first")
        assertEquals("noteB",notes[0].title)
        assertEquals("noteA",notes[1].title)
    }
    @Test
    fun sort_notes_when_a_to_z_returns_the_right_order() {
        SortNotes.sortNotes(notes,"A to Z")
        assertEquals("noteA",notes[0].title)
        assertEquals("noteB",notes[1].title)
    }
    @Test
    fun sort_notes_when_z_to_a_returns_the_right_order() {
        SortNotes.sortNotes(notes,"Z to A")
        assertEquals("noteB",notes[0].title)
        assertEquals("noteA",notes[1].title)
    }
    @Test
    fun sort_notes_when_title_null_returns_it_fist() {
        notes.add(Note(null,"",LocalDateTime.now().minusMinutes(2).toString()))
        SortNotes.sortNotes(notes,"A to Z")
        assertEquals(null,notes[0].title)
        assertEquals("noteA",notes[1].title)
        assertEquals("noteB",notes[2].title)
    }
    @Test
    fun sort_notes_when_title_empty_returns_it_fist() {
        notes.add(Note("","",LocalDateTime.now().minusMinutes(2).toString()))
        SortNotes.sortNotes(notes,"A to Z")
        assertEquals("",notes[0].title)
        assertEquals("noteA",notes[1].title)
        assertEquals("noteB",notes[2].title)
    }
    @Test
    fun sort_notes_when_date_empty_returns_it_last() {
        notes.add(Note("noteC","",""))
        SortNotes.sortNotes(notes,"Newest first")
        assertEquals("noteA",notes[0].title)
        assertEquals("noteB",notes[1].title)
        assertEquals("noteC",notes[2].title)
    }
}