package com.jetpack.paypay.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetpack.paypay.data.dtos.CurrencyDto
import com.jetpack.paypay.data.entities.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyConverterDao {
  @Query("SELECT * FROM currency")
  fun getCurrencies(): CurrencyEntity

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(collections: CurrencyEntity)

  @Query("Select timestamp from currency")
  fun getTimeStamp():Long?

}