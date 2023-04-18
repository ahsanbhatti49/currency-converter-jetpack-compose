package com.jetpack.converter.domain

import com.jetpack.converter.data.ResponseState
import com.jetpack.converter.data.entities.CurrencyEntity
import kotlinx.coroutines.flow.Flow


interface CurrencyConverterRepository {
  suspend fun getAllCurrencies(fetchFromRemote: Boolean = true): Flow<ResponseState<CurrencyEntity>>
}