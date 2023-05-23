package com.rgnotes.notesapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgnotes.notesapp.data.utils.User
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import com.rgnotes.notesapp.data.utils.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: RepositoryAuthInterface,
    private val dataRepo: RepositoryDataInterface
) :
    ViewModel() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _status = MutableSharedFlow<Status?>(replay = 1)
    val status: MutableSharedFlow<Status?> = _status

    suspend fun clearUpdate() {
        _status.emit(null)
    }

    fun registerUser(email: String?, password: String?, user: User) {
        viewModelScope.launch {
            if (Validate.email(email)) {
                if(Validate.password(password)){
                    withContext(ioDispatcher) {
                        authRepo.registerUser(email!!, password!!).collect { _status.emit(it) }
                    }
                    withContext(ioDispatcher) {
                        dataRepo.setUserData(user).collect { _status.emit(it) }
                    }
                }else{ _status.emit(AuthStatus.Error("Invalid password format!"))}
            }else{ _status.emit(AuthStatus.Error("Invalid email format!"))}
        }
    }
}