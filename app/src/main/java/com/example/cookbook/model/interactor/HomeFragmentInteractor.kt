package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest

class HomeFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryInfoImpl
) {

    suspend fun getJokeText(): AppState {
        return AppState.Success(remoteRepository.getJokeText())
    }
}