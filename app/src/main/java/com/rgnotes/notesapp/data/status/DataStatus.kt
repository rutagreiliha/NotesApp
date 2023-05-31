package com.rgnotes.notesapp.data.status

sealed class DataStatus : Status() {
    object Initial : DataStatus()
    object Loading : DataStatus()
    data class GetNote<T>(val data: T) : DataStatus()
    data class SetNote<T>(val data: T) : DataStatus()
    data class DeleteNote<T>(val data: T) : DataStatus()
    data class Error(val message: String?) : DataStatus()
}