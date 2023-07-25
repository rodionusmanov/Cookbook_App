package com.example.cookbook.model.repository.network

import androidx.lifecycle.LiveData
import com.example.cookbook.utils.network.NetworkLiveData

class NetworkRepository(
    private val networkLiveData: NetworkLiveData
) {

    fun getNetworkStatusLiveData(): LiveData<Boolean> = networkLiveData

}