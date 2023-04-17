package com.jetpack.paypay.data.dtos

data class CurrencyDto(
  val base: String,
  val disclaimer: String,
  val license: String,
  val rates: Map<String, Double>,
  val timestamp: Long
)