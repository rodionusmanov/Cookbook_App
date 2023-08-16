package com.example.cookbook.model.datasource

import com.example.cookbook.model.datasource.DTO.joke.JokeDTO
import retrofit2.Response

interface JokeDataSource {

    suspend fun getJokeText(): Response<JokeDTO>
}