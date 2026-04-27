package com.example.pdvkotlin.data.repository

import com.example.pdvkotlin.data.local.SeedData
import com.example.pdvkotlin.data.local.dao.ProductDao
import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
) : ProductRepository {
    override fun observeProducts(query: String): Flow<List<Product>> =
        productDao.observeProducts(query).map { products -> products.map { it.toDomain() } }

    override suspend fun getByBarcode(barcode: String): Product? =
        productDao.getByBarcode(barcode)?.toDomain()

    override suspend fun upsert(product: Product) {
        productDao.upsert(product.toEntity())
    }

    override suspend fun seedInitialProducts() {
        if (productDao.count() == 0) {
            productDao.upsertAll(SeedData.products.map { it.toEntity(pendingSeedTimestamp) })
        }
    }

    private companion object {
        const val pendingSeedTimestamp = 1L
    }
}
