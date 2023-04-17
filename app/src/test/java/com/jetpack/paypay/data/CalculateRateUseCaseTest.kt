package com.jetpack.paypay.data

import com.jetpack.paypay.data.dtos.LocalCurrencyDto
import com.jetpack.paypay.data.entities.CurrencyEntity
import com.jetpack.paypay.domain.CurrencyConverterRepository
import com.jetpack.paypay.domain.usecase.CalculateRateUsecase
import com.jetpack.paypay.domain.usecase.GetAllCurrenciesUsecase
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
class CalculateRateUseCaseTest {
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

      val data = mutableListOf<LocalCurrencyDto>()

      data.addAll(listOf(LocalCurrencyDto(
        rate = 6.0,
        currency = "AFN"
      ),LocalCurrencyDto(
        rate = 4.0,
        currency = "AED"
      )))
      val useCase = CalculateRateUsecase()

      useCase.invoke(data,2.0).collect{ data->
        assert(data[0].rate.equals(12.0))
      }
    }
  }
}