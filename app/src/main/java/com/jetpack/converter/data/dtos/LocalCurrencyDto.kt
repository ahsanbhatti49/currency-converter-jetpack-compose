package com.jetpack.converter.data.dtos

data class LocalCurrencyDto(
  val id: Int? = -1,
  val currency: String,
  var rate: Double
)