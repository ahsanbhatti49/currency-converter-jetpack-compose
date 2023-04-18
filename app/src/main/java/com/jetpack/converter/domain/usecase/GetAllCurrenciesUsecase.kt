package com.jetpack.converter.domain.usecase

import com.jetpack.converter.data.ResponseState
import com.jetpack.converter.data.entities.CurrencyEntity
import com.jetpack.converter.domain.CurrencyConverterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllCurrenciesUsecase
@Inject constructor(private val currencyConverterRepository: CurrencyConverterRepository) {
  suspend operator fun invoke(): Flow<ResponseState<CurrencyEntity>> {
    return currencyConverterRepository.getAllCurrencies()
  }
}