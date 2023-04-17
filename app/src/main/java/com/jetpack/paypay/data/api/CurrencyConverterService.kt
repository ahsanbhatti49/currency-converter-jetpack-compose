package com.jetpack.paypay.data.api

import com.jetpack.paypay.data.dtos.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyConverterService {

  @GET("latest.json")
  suspend fun getAllCurrencies(@QueryMap params: HashMap<String, String>): CurrencyDto

}