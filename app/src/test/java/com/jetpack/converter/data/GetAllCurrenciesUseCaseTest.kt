package com.jetpack.converter.data

import com.jetpack.converter.data.entities.CurrencyEntity
import com.jetpack.converter.domain.CurrencyConverterRepository
import com.jetpack.converter.domain.usecase.GetAllCurrenciesUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetAllCurrenciesUseCaseTest {
  private lateinit var currencyConverterRepository: CurrencyConverterRepository
  @Before
  fun setUp() {
    currencyConverterRepository = mock(CurrencyConverterRepository::class.java)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun getAllCurrenciesShouldReturnAListOfCurrencies() {
    runTest {
      val rates = HashMap<String, Double>()
      rates["AED"] = 3.6
      rates["AFN"] = 89.13
      val currencyEntity = CurrencyEntity(
        timestamp = 1674932405,
        rates = rates,
        baseCurrency = "USD"
      )
      whenever(currencyConverterRepository.getAllCurrencies()).thenReturn(
        flow {
          emit(ResponseState.Success(currencyEntity))
        }
      )
      val useCase = GetAllCurrenciesUsecase(currencyConverterRepository)
      useCase.invoke().collect{ data->
        when(data){
          is ResponseState.Success ->{
            assert(data.data.rates.isNotEmpty())
            assert(data.data.rates["AED"]==3.6)
            assert(data.data.rates["AFN"]==89.13)
          }
          else -> {}
        }
      }
    }
  }
}