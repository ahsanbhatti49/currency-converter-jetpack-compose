package com.jetpack.converter.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.converter.data.ResponseState
import com.jetpack.converter.data.dtos.LocalCurrencyDto
import com.jetpack.converter.domain.usecase.CalculateRateUsecase
import com.jetpack.converter.domain.usecase.ChangeBaseCurrencyUseCase
import com.jetpack.converter.domain.usecase.GetAllCurrenciesUsecase
import com.jetpack.converter.ui.states.GetCurrencyListUIStates
import com.jetpack.converter.ui.states.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel
class CurrencyConverterVm @Inject constructor(
  private val getAllCurrenciesUsecase: GetAllCurrenciesUsecase,
  private val calculateRateUsecase: CalculateRateUsecase,
  private val changeBaseCurrencyUseCase: ChangeBaseCurrencyUseCase,
) : ViewModel(), GetCurrencyListUIStates, HomeScreenState {
  private var _dataList = mutableListOf<LocalCurrencyDto>()
  private val _baseCurrencyState= MutableStateFlow("USD")//TODO update this and pick from history data in cache
  val baseCurrencyState: StateFlow<String> = _baseCurrencyState.asStateFlow()

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
        viewModelScope.launch {
          isLoading.emit(true)
          _baseCurrencyState.value = event.selectedBaseCurrency.currency

          val updatedList = changeBaseCurrencyUseCase.invoke(_dataList, baseRate = event.selectedBaseCurrency.rate).first()
          data.emit(updatedList.toList())
          isLoading.emit(false)
        }

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