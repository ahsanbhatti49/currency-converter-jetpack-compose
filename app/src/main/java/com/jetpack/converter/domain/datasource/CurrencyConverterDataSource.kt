package com.jetpack.converter.domain.datasource

import com.jetpack.converter.BuildConfig
import com.jetpack.converter.data.ResponseState
import com.jetpack.converter.data.api.CurrencyConverterService
import com.jetpack.converter.data.entities.CurrencyEntity
import com.jetpack.converter.data.local.CurrencyConverterDao
import com.jetpack.converter.domain.CurrencyConverterRepository
import com.jetpack.converter.utils.ApiErrorHandling
import com.jetpack.converter.utils.Constants.API_ID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CurrencyConverterDataSource @Inject constructor(
  private val currencyConverterDao: CurrencyConverterDao,
  private val currencyConverterService: CurrencyConverterService
) : CurrencyConverterRepository {
  private val CACHE_EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000L // 24 hours

  private suspend fun refreshData() {
    val param = HashMap<String, String>().apply { put(API_ID, BuildConfig.API_ID) }
    currencyConverterService.getAllCurrencies(param).also {
      currencyConverterDao.insertAll(
        CurrencyEntity(
          timestamp = it.timestamp,
          rates = it.rates,
          baseCurrency = it.base
        )
      )
    }
  }

  override suspend fun getAllCurrencies(fetchFromRemote: Boolean) = flow {
    emit(ResponseState.Loading(true))
    val timestamp = currencyConverterDao.getTimeStamp()?:-1L

    if (isCacheExpired(timestamp)){
      refreshData()
    }
    val response = currencyConverterDao.getCurrencies()

    emit(ResponseState.Loading(false))
    emit(ResponseState.Success(response))
  }.catch { e ->
    emit(ResponseState.Loading(false))
    emit(
      ResponseState.Failure(
        ApiErrorHandling.error(
          e,
          isCancelled = true
        )
      )
    )
  }.flowOn(Dispatchers.IO)

  private fun isCacheExpired(timestamp: Long): Boolean {
    val elapsed = System.currentTimeMillis() - timestamp
    return elapsed >= CACHE_EXPIRATION_TIME_MS
  }

}