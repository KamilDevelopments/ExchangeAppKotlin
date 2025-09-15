package com.kamildevelopments.exchangeappkotlin.api

import com.kamildevelopments.exchangeappkotlin.api.RatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("apikey") apiKey: String,
        @Query("currencies") currencies: String
    ): RatesResponse
}