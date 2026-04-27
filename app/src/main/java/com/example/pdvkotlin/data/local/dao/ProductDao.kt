package com.example.pdvkotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pdvkotlin.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query(
        """
        SELECT * FROM products
        WHERE isActive = 1
        AND (:query = '' OR name LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%')
        ORDER BY name
        """
    )
    fun observeProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getByBarcode(barcode: String): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)
}
