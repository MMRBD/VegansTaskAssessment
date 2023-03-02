package com.example.veganstaskassessment.data.network

import com.example.veganstaskassessment.data.models.Media
import retrofit2.http.GET

interface ApiInterface {
    @GET("test.json")
    suspend fun getMedias(): Media
}