package com.example.pdvkotlin.domain.repository

import com.example.pdvkotlin.domain.model.ReportSummary
import com.example.pdvkotlin.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun observeHeldSales(): Flow<List<Sale>>
    fun observePaidSales(): Flow<List<Sale>>
    suspend fun saveSale(sale: Sale)
    suspend fun reports(): ReportSummary
}
