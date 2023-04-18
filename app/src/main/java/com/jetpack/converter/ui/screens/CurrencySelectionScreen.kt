package com.jetpack.converter.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetpack.converter.data.dtos.LocalCurrencyDto
import com.jetpack.converter.ui.states.GetCurrencyListUIStates
import com.jetpack.converter.vm.CurrencyConverterEvent
import com.jetpack.converter.vm.CurrencyConverterVm

const val TEST_CURRENCY_TAG_DISPLAYED: String = "TEST_CURRENCY_TAG_DISPLAYED"

@Composable
fun CurrencySelectionScreen(
  uiStates: GetCurrencyListUIStates,
  viewModel: CurrencyConverterVm,
  navController: NavController,
) {
  val loading = uiStates.isLoading.collectAsState()
  val error = uiStates.error.collectAsState()
  val data = uiStates.data.collectAsState()

  Scaffold(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = MaterialTheme.colors.primarySurface
  ) {
    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
    ) {

      error.value?.let { it1 -> errorComposable(error = it1) }
      loader(loading.value)

      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = rememberLazyListState()
      ) {
        itemsIndexed(data.value) { _, currency ->
          CurrencyView(
            currencyData = currency,
            onClick = {
              viewModel.onEvent(CurrencyConverterEvent.OnCurrencySelected(it))
              navController.popBackStack()
            }
          )
        }
      }
    }
  }
}

@Composable
fun loader(value: Boolean) {
  AnimatedVisibility(visible = value) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Black.copy(alpha = .2f)),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(color = Color.Red)
    }
  }
}

@Composable
fun errorComposable(error: String) {
  Text(
    text = error,
    color = Color.Red,
    style = MaterialTheme.typography.body2
  )
}

@Composable
fun CurrencyView(
  currencyData: LocalCurrencyDto,
  onClick: (LocalCurrencyDto) -> Unit,
  modifier: Modifier = Modifier,
  showRate: Boolean = false
) {
  Card(
    shape = MaterialTheme.shapes.small,
    elevation = 16.dp,
    modifier = modifier
      .fillMaxWidth()
      .padding(2.dp)
      .clickable {
        onClick(currencyData)
      },
    backgroundColor = MaterialTheme.colors.primary
  ) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .testTag(TEST_CURRENCY_TAG_DISPLAYED)
    ) {
      Text(
        text = currencyData.currency.uppercase(),
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier.align(Alignment.Start)
      )
      if (showRate)
        Text(
          modifier = Modifier.align(Alignment.End),
          text = String.format("%.3f", currencyData.rate),
          color = MaterialTheme.colors.onPrimary
        )
    }
  }
}

