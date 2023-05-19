package com.rgnotes.notesapp.data.status

sealed class DataStatus : Status() {
    class Loading() : DataStatus()
    class GetNote<T>(val data: T) : DataStatus()
    class SetNote<T>(val data: T) : DataStatus()
    class DeleteNote<T>(val data: T) : DataStatus()
    class Error(val message: String?) : DataStatus()
}