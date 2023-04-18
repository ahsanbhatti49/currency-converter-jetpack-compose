package com.jetpack.converter.domain.usecase

import com.jetpack.converter.data.dtos.LocalCurrencyDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CalculateRateUsecase
@Inject constructor() {
  suspend operator fun invoke(
    _dataList: MutableList<LocalCurrencyDto>,
    baseRate: Double,
  ): Flow<List<LocalCurrencyDto>> {
    val convertedCurrencies : MutableList<LocalCurrencyDto> = mutableListOf()
    return flow {
      _dataList.forEach {
        convertedCurrencies.add(LocalCurrencyDto(it.id,it.currency,getConvertedCurrencyRate(1.0,it.rate,baseRate)))
      }
      emit(convertedCurrencies)
    }
  }
  fun getConvertedCurrencyRate(fromCurrency:Double,toCurrency:Double,amount:Double): Double {
    return amount * (toCurrency / fromCurrency)
  }
}