package com.rgnotes.notesapp.data.viewmodel

import com.rgnotes.notesapp.data.repo.RepositoryAuthInterface
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.Status
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SignInViewModelTest {
    private val repo = mock<RepositoryAuthInterface>()
    private lateinit var viewModel: SignInViewModel

    @Before
    fun setup() {
        viewModel = SignInViewModel(repo, UnconfinedTestDispatcher())
        Dispatchers.setMain(UnconfinedTestDispatcher())

    }

    @Test
    fun sign_in_function_when_correct_login_returns_loading_then_auth_success_state() {
        runTest(UnconfinedTestDispatcher()) {
            whenever(repo.signInUser("email.email@email.com", "password123")).thenReturn(
                flowOf(AuthStatus.Initial(), AuthStatus.Success("userid"))
            )
            val actual = mutableListOf<Status>()
            val job = launch {
                viewModel.signInUser("email.email@email.com", "password123")
                viewModel.status.collect { it ->
                    if (it != null) {
                        actual.add(it)
                    }
                }
            }
            assertTrue(actual[0] is AuthStatus.Initial)
            assertTrue(actual[1] is AuthStatus.Success<*>)
            job.cancel()
        }
    }

    @Test
    fun sign_in_function_when_wrong_login_returns_loading_then_auth_error_state() {
        runTest(UnconfinedTestDispatcher()) {
            whenever(repo.signInUser("email.email@email.com", "password123")).thenReturn(
                flowOf(AuthStatus.Initial(), AuthStatus.Success("userid"))
            )
            whenever(repo.signInUser("wrong.email@email.com", "password123")).thenReturn(
                flowOf(AuthStatus.Initial(), AuthStatus.Error("error"))
            )
            val actual = mutableListOf<Status>()
            val job = launch {
                viewModel.signInUser("wrong.email@email.com", "password123")
                viewModel.status.collect {
                    if (it != null) {
                        actual.add(it)
                    }
                }
            }
            assertTrue(actual[0] is AuthStatus.Initial)
            assertTrue(actual[1] is AuthStatus.Error)
            job.cancel()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}