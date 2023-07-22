package com.example.cookbook.model.datasource

interface DataSource<T> {
    suspend fun getData(request: String) : T
}