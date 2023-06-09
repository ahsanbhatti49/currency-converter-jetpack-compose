package com.jetpack.converter.di

import android.content.Context
import androidx.room.Room
import com.jetpack.converter.data.local.AppDatabase
import com.jetpack.converter.data.api.CurrencyConverterService
import com.jetpack.converter.data.local.CurrencyConverterDao
import com.jetpack.converter.domain.CurrencyConverterRepository
import com.jetpack.converter.domain.datasource.CurrencyConverterDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  /**
   * Provides app AppDatabase
   */
  @Singleton
  @Provides
  fun provideDb(@ApplicationContext context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "currency-db")
      .fallbackToDestructiveMigration().build()

  /**
   * Provides collectionDao an object to access NewsArticles table from Database
   */
  @Singleton
  @Provides
  fun provideCollectionDao(db: AppDatabase): CurrencyConverterDao = db.currencyConverterDao()

  @Singleton
  @Provides
  fun provideCurrencyConverterRepository(
    currencyConverterDao: CurrencyConverterDao,
    currencyConverterService: CurrencyConverterService
  ): CurrencyConverterRepository = CurrencyConverterDataSource(
    currencyConverterDao = currencyConverterDao,
    currencyConverterService = currencyConverterService
  )
}