package com.jetpack.converter.domain.usecase

import com.jetpack.converter.data.dtos.LocalCurrencyDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChangeBaseCurrencyUseCase
@Inject constructor() {
  suspend operator fun invoke(
    _dataList: MutableList<LocalCurrencyDto>,
    baseRate: Double,
  ): Flow<List<LocalCurrencyDto>> {
    return flow {
      _dataList.map {
        it.rate = getConvertedCurrencyRate(it.rate,1.0,baseRate)
      }
      emit(_dataList)
    }
  }
  fun getConvertedCurrencyRate(fromCurrency:Double,toCurrency:Double,amount:Double): Double {
    return amount * (toCurrency / fromCurrency)
  }
}