package com.example.pdvkotlin.domain.repository

import com.example.pdvkotlin.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(query: String = ""): Flow<List<Product>>
    suspend fun getByBarcode(barcode: String): Product?
    suspend fun upsert(product: Product)
    suspend fun seedInitialProducts()
}
