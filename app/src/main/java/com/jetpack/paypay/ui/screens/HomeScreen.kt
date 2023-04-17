package com.jetpack.paypay.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetpack.paypay.data.dtos.LocalCurrencyDto
import com.jetpack.paypay.ui.states.HomeScreenState
import com.jetpack.paypay.utils.Constants
import com.jetpack.paypay.vm.CurrencyConverterEvent
import com.jetpack.paypay.vm.CurrencyConverterVm
import java.util.Currency
import kotlin.random.Random

@Composable
fun HomeScreen(
  uiStates: HomeScreenState,
  viewModel: CurrencyConverterVm,
  navController: NavController
) {
  val loading = uiStates.isLoading.collectAsState()
  val error = uiStates.error.collectAsState()
  val data = uiStates.data.collectAsState()

  Scaffold {
    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
    ) {
      Text(
        text = "Currency Converter",
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        style = MaterialTheme.typography.h4
      )

      Spacer(modifier = Modifier.height(8.dp))

      CurrencyCard(
        currency = LocalCurrencyDto(id = Random.nextInt(), "USD", 23.2),
        viewModel = viewModel,
        navController = navController
      )

      error.value?.let { it1 -> errorComposable(error = it1) }
      loader(loading.value)

      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = rememberLazyListState()
      ) {
        items(data.value, key = { it.id!! }) {
          CurrencyView(
            currencyData = it,
            onClick = {
            }, showRate = true
          )
        }
      }

    }
  }
}


@Composable
fun CurrencyCard(
  currency: LocalCurrencyDto,
  modifier: Modifier = Modifier,
  viewModel: CurrencyConverterVm,
  navController: NavController
) {

  val focusManager = LocalFocusManager.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp),
    shape = MaterialTheme.shapes.small,
    elevation = 16.dp,
  ) {
    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
    ) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(MaterialTheme.shapes.medium)
          .clickable {
            viewModel.onEvent(CurrencyConverterEvent.fetchAllCurrencies)
            navController.navigate(Constants.CURRENCY_SELECTION_SCREEN)
          }
      ) {
        Column(
          verticalArrangement = Arrangement.SpaceAround,
          horizontalAlignment = Alignment.Start,
          modifier = Modifier.padding(8.dp)
        ) {
          Text(
            text = currency.currency.uppercase(),
          )
        }
        Icon(
          painter = rememberVectorPainter(image = Icons.Filled.ArrowForward),
          contentDescription = "right arrow",
          modifier = Modifier
            .padding(end = 16.dp)
            .size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      val text = remember { mutableStateOf(TextFieldValue("")) }
      OutlinedTextField(
        value = text.value,
        onValueChange = {
          text.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
          Text(text = Currency.getInstance(currency.currency).symbol)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            focusManager.clearFocus()
          }
        ),
        label = { Text("Amount") },

      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        onClick = {
          if (!text.value.text.isNullOrBlank())
            viewModel.onEvent(CurrencyConverterEvent.OnCalculateClickEvent(text.value.text.toDouble()))
        },
        modifier = Modifier.fillMaxWidth(),

        ) {
        Text(text = "Convert", color = Color.White)

      }
    }
  }
}