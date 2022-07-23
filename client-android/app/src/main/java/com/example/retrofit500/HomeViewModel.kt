package com.example.retrofit500

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Response

class HomeViewModel(
    private val orderService: OrderService
) : ViewModel() {

    private val parentJob = SupervisorJob()

    private val localScope = CoroutineScope(Dispatchers.Main + parentJob)

    var state by mutableStateOf(HomeState())
        private set

    fun onGetOrder200Click() {
        localScope.launchCatchError(
            block = {
                val result = try {
                    Success(orderService.getOrder200())
                } catch (throwable: Throwable) {
                    Fail(throwable)
                }
                when (result) {
                    is Success -> onSuccess(result.data)
                    is Fail -> onError(result.throwable)
                }
            })
        {
            if (it !is CancellationException) {
                onError(it)
            } else {
                Log.d("AppDebug", "Other errors")
            }
        }
    }

    fun onGetOrder500Click() {
        localScope.launchCatchError(
            block = {
                val result = try {
                    Success(orderService.getOrder500())
                } catch (throwable: Throwable) {
                    Fail(throwable)
                }
                when (result) {
                    is Success -> onSuccess(result.data)
                    is Fail -> onError(result.throwable)
                }
            })
        {
            if (it !is CancellationException) {
                onError(it)
            } else {
                Log.d("AppDebug", "Other errors")
            }
        }
    }

    fun onPostOrder500Click() {
        localScope.launchCatchError(
            block = {
                val result = try {
                    Success(orderService.postOrder500())
                } catch (throwable: Throwable) {
                    Fail(throwable)
                }
                when (result) {
                    is Success -> onSuccess(result.data)
                    is Fail -> onError(result.throwable)
                }
            })
        {
            if (it !is CancellationException) {
                onError(it)
            } else {
                Log.d("AppDebug", "Other errors")
            }
        }
    }

    private fun onSuccess(result: Response<Order>) {
        if (result.isSuccessful && result.body() != null) {
            result.body()?.let {
                state = state.copy(text = it.message)
            } ?: throw MessageErrorException("Empty body")
        } else {
            throw MessageErrorException(result.getErrorString())
        }
    }

    private fun onError(throwable: Throwable) {
        Log.d("AppDebug", "Error: $throwable")
        state = state.copy(text = throwable.message ?: "onError")
    }
}
