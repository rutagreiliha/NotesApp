package com.rgnotes.notesapp.data.viewmodel

import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.repo.RepositoryDataInterface
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SignInViewModelTest {
    private val repo = mock<RepositoryAuthInterface>()
    private lateinit var viewModel:SignInViewModel
    @Before
    fun setup(){
        viewModel = SignInViewModel(repo)
    }
//TODO: test flow

}