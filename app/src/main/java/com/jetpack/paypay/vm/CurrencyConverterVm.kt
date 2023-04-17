package com.jetpack.paypay.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.paypay.data.ResponseState
import com.jetpack.paypay.data.dtos.LocalCurrencyDto
import com.jetpack.paypay.domain.usecase.CalculateRateUsecase
import com.jetpack.paypay.domain.usecase.GetAllCurrenciesUsecase
import com.jetpack.paypay.ui.states.GetCurrencyListUIStates
import com.jetpack.paypay.ui.states.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel
class CurrencyConverterVm @Inject constructor(
  private val getAllCurrenciesUsecase: GetAllCurrenciesUsecase,
  private val calculateRateUsecase: CalculateRateUsecase
) : ViewModel(), GetCurrencyListUIStates, HomeScreenState {
  private var _dataList = mutableListOf<LocalCurrencyDto>()

  init {
    loadCurrencies()
  }

  private fun loadCurrencies() {
    viewModelScope.launch {
      getAllCurrenciesUsecase.invoke().collect() {
        when (it) {
          is ResponseState.Loading -> {
            isLoading.emit(it.isLoading)
          }
          is ResponseState.Failure -> {
            error.emit(it.errorMessage)
          }
          is ResponseState.Success -> {
            val dataList = mutableListOf<LocalCurrencyDto>()
            it.data.rates.forEach { currency ->
              dataList.add(
                LocalCurrencyDto(
                  id = Random.nextInt(),
                  currency = currency.key,
                  rate = currency.value
                )
              )
            }
            data.emit(dataList)
            _dataList = dataList
          }
        }
      }
    }
  }

  fun onEvent(event: CurrencyConverterEvent) {
    when (event) {
      is CurrencyConverterEvent.OnCurrencySelected -> {
        Log.d("done","done")

      }
      is CurrencyConverterEvent.OnCalculateClickEvent -> {
        viewModelScope.launch {
          isLoading.emit(true)
          val updatedList = calculateRateUsecase.invoke(_dataList, baseRate = event.amount).first()
          data.emit(updatedList.toList())
          isLoading.emit(false)
        }
      }
    }
  }

  override val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
  override val error: MutableStateFlow<String?> = MutableStateFlow(null)
  override val data: MutableStateFlow<List<LocalCurrencyDto>> = MutableStateFlow(emptyList())

}