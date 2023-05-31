package com.rgnotes.notesapp.data.viewmodel

import app.cash.turbine.test
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SignInViewModelTest {
    private val repo = mock<RepositoryAuthInterface>()
    private lateinit var viewModel:SignInViewModel
    @Before
    fun setup(){
        viewModel = SignInViewModel(repo,UnconfinedTestDispatcher())
        Dispatchers.setMain(UnconfinedTestDispatcher())

    }

    @Test
    fun sign_in_function_when_correct_login_returns_auth_success_state(){
        runTest (UnconfinedTestDispatcher()) {
             whenever(repo.signInUser("email.email@email.com","password123")).thenReturn(
                flowOf(AuthStatus.Initial(),AuthStatus.Success("1"))
            )
            val actual = mutableListOf<Status>()
            val job = launch{
                viewModel.signInUser("email.email@email.com","password123")
                 viewModel.status.collect{it ->
                     if (it != null) {
                         actual.add(it)
                     }
                 }
            }
            assertThat(actual[0], instanceOf(AuthStatus.Initial::class.java))
            assertThat(actual[1], instanceOf(AuthStatus.Success::class.java))
            job.cancel()
    }
}
@After
fun tearDown() {
    Dispatchers.resetMain()
}
}