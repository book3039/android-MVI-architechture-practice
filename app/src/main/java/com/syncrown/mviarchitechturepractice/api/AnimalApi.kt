package com.syncrown.mviarchitechturepractice.api

import com.syncrown.mviarchitechturepractice.model.Animal
import retrofit2.http.GET

interface AnimalApi {

    @GET("animals.json")
    suspend fun getAnimals(): List<Animal>
}