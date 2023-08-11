package com.example.cookbook.model

sealed class AppState {

    data class Success<T>(val data: T) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}