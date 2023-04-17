package com.jetpack.paypay.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jetpack.paypay.data.local.Converter
import java.math.BigDecimal


@Entity(tableName = "currency")
data class CurrencyEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val timestamp: Long,
  @field:TypeConverters(Converter::class)
  val rates: Map<String, Double>,
  var baseCurrency: String
)