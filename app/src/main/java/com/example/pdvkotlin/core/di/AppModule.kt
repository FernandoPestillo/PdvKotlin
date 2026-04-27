package com.example.pdvkotlin.core.di

import android.content.Context
import androidx.room.Room
import com.example.pdvkotlin.data.local.PdvDatabase
import com.example.pdvkotlin.data.remote.FakePdvApi
import com.example.pdvkotlin.data.repository.AuthRepositoryImpl
import com.example.pdvkotlin.data.repository.ProductRepositoryImpl
import com.example.pdvkotlin.data.repository.SaleRepositoryImpl
import com.example.pdvkotlin.domain.repository.AuthRepository
import com.example.pdvkotlin.domain.repository.ProductRepository
import com.example.pdvkotlin.domain.repository.SaleRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PdvDatabase =
        Room.databaseBuilder(context, PdvDatabase::class.java, "pdv.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideProductDao(database: PdvDatabase) = database.productDao()

    @Provides
    fun provideSaleDao(database: PdvDatabase) = database.saleDao()

    @Provides
    fun provideAuditLogDao(database: PdvDatabase) = database.auditLogDao()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides
    @Singleton
    fun provideFakeApi(client: OkHttpClient, json: Json): FakePdvApi =
        Retrofit.Builder()
            .baseUrl("https://example.com/api/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FakePdvApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(repository: SaleRepositoryImpl): SaleRepository
}
