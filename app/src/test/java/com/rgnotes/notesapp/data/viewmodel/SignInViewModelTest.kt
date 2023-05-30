package com.rgnotes.notesapp.data.viewmodel

import app.cash.turbine.test
import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
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

//TODO: test flow


    @Test
    fun sign_in_function_when_correct_login_returns_auth_success_state(){

        runTest(UnconfinedTestDispatcher()) {
            viewModel = SignInViewModel(repo,UnconfinedTestDispatcher())
            val job = launch { whenever(repo.signInUser("email.email@email.com","password123")).thenReturn(
                flowOf(AuthStatus.Success("1"))
            ) }

            val expected = AuthStatus.Success("1")
            viewModel.signInUser("email.email@email.com","password123")
            advanceUntilIdle()
            viewModel.status.test {
                val actual = viewModel.status.value
                assertEquals(expected, actual)
                job.cancel()
            }


    }

}
@After
fun tearDown() {
    Dispatchers.resetMain()
}
}