package com.jetpack.converter.ui.states

import com.jetpack.converter.data.dtos.LocalCurrencyDto
import kotlinx.coroutines.flow.StateFlow

interface GetCurrencyListUIStates {
  val isLoading: StateFlow<Boolean>
  val error: StateFlow<String?>
  val data: StateFlow<List<LocalCurrencyDto>>
}
