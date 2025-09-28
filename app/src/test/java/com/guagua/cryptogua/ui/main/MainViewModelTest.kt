package com.guagua.cryptogua.ui.main

import com.guagua.cryptogua.domain.connection.ConnectUseCase
import com.guagua.cryptogua.model.error.CryptoGuaException
import com.guagua.cryptogua.model.ws.WsStatus
import com.guagua.cryptogua.ui.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var connectUseCase: ConnectUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        connectUseCase = mockk()
    }

    @Test
    fun `should update isConnecting to true when status is Connecting`() = runTest {
        every { connectUseCase() } returns flowOf(WsStatus.Connecting)

        viewModel = MainViewModel(connectUseCase)
        runCurrent()

        val state = viewModel.state.value
        assertTrue(state.isConnecting)
        assertFalse(state.hasConnectionIssue)
    }

    @Test
    fun `should update isConnecting to false when status is Connected`() = runTest {
        every { connectUseCase() } returns flowOf(WsStatus.Connected)

        viewModel = MainViewModel(connectUseCase)

        val state = viewModel.state.value
        assertFalse(state.isConnecting)
        assertFalse(state.hasConnectionIssue)
    }

    @Test
    fun `should set hasConnectionIssue to true after 3 failed retries`() = runTest {
        val exception = CryptoGuaException.Connection("Connection failed", null)
        var count = 0
        every { connectUseCase() } returns flow {
            if (count <= 3) { // Limit for test ability
                count++
                throw exception
            }
        }

        viewModel = MainViewModel(connectUseCase)
        runCurrent()

        // Wait for first attempt
        advanceTimeBy(9000)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.hasConnectionIssue)
    }

    @Test
    fun `should reset hasConnectionIssue when connection succeeds after failures`() = runTest {
        val exception = CryptoGuaException.Connection("Connection failed", null)
        var count = 0
        every { connectUseCase() } returns flow {
            emit(WsStatus.Connecting)
            if (count <= 3) { // Limit for test ability
                count++
                throw exception
            }
        }

        viewModel = MainViewModel(connectUseCase)
        runCurrent()

        // Wait for first attempt
        advanceTimeBy(9000)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.hasConnectionIssue)
    }
}