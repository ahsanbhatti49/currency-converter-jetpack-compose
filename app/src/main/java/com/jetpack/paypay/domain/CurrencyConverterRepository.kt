package com.jetpack.paypay.domain

import com.jetpack.paypay.data.ResponseState
import com.jetpack.paypay.data.entities.CurrencyEntity
import kotlinx.coroutines.flow.Flow


interface CurrencyConverterRepository {
  suspend fun getAllCurrencies(fetchFromRemote: Boolean = true): Flow<ResponseState<CurrencyEntity>>
}