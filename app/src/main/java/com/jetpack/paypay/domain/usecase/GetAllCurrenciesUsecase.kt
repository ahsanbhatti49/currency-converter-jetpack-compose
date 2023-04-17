package com.jetpack.paypay.domain.usecase

import com.jetpack.paypay.data.ResponseState
import com.jetpack.paypay.data.entities.CurrencyEntity
import com.jetpack.paypay.domain.CurrencyConverterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllCurrenciesUsecase
@Inject constructor(private val currencyConverterRepository: CurrencyConverterRepository) {
  suspend operator fun invoke(): Flow<ResponseState<CurrencyEntity>> {
    return currencyConverterRepository.getAllCurrencies()
  }
}