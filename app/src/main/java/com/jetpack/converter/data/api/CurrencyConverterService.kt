package com.jetpack.converter.data.api

import com.jetpack.converter.data.dtos.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyConverterService {

  @GET("latest.json")
  suspend fun getAllCurrencies(@QueryMap params: HashMap<String, String>): CurrencyDto

}