package com.jetpack.paypay.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetpack.paypay.ui.screens.CurrencySelectionScreen
import com.jetpack.paypay.ui.screens.HomeScreen
import com.jetpack.paypay.ui.states.GetCurrencyListUIStates
import com.jetpack.paypay.ui.states.HomeScreenState
import com.jetpack.paypay.ui.theme.PayPayTheme
import com.jetpack.paypay.utils.Constants.CURRENCY_SELECTION_SCREEN
import com.jetpack.paypay.utils.Constants.Home_Screen
import com.jetpack.paypay.vm.CurrencyConverterVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel: CurrencyConverterVm by viewModels()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val currencySelectorPageState: GetCurrencyListUIStates = viewModel
    val homeScreenState: HomeScreenState = viewModel
    setContent {
      PayPayTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          val navController = rememberNavController()

          NavHost(navController = navController, startDestination = Home_Screen) {
            composable(Home_Screen) {
              HomeScreen(
                uiStates = homeScreenState,
                navController = navController,
                viewModel = viewModel
              )
            }
            composable(CURRENCY_SELECTION_SCREEN) {
              CurrencySelectionScreen(
                uiStates = currencySelectorPageState,
                viewModel = viewModel,
                navController = navController
              )
            }
          }
        }
      }
    }
  }
}
