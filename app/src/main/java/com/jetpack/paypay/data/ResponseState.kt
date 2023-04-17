package com.jetpack.paypay.data

import com.jetpack.paypay.data.entities.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector


sealed class ResponseState<T> : Flow<CurrencyEntity> {
  data class Loading<T>(val isLoading: Boolean) : ResponseState<T>()
  data class Failure<T>(val errorMessage: String) : ResponseState<T>()
  data class Idle<T>(val idle: Boolean) : ResponseState<T>()
  data class Success<T>(val data: T) : ResponseState<T>()

  override suspend fun collect(collector: FlowCollector<CurrencyEntity>) {
    TODO("Not yet implemented")
  }
}
