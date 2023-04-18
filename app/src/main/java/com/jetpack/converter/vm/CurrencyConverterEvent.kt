package com.jetpack.converter.vm

import com.jetpack.converter.data.dtos.LocalCurrencyDto


sealed class CurrencyConverterEvent {
  object fetchAllCurrencies : CurrencyConverterEvent()
  data class OnCalculateClickEvent(val amount: Double) : CurrencyConverterEvent()
  data class OnUpdateBaseCurrency(val currentCurrency: String) : CurrencyConverterEvent()
  data class OnCurrencySelected(val selectedBaseCurrency: LocalCurrencyDto,) :
    CurrencyConverterEvent()
}