package com.example.pdvkotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pdvkotlin.data.local.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales WHERE status = 'HELD' ORDER BY createdAt DESC")
    fun observeHeldSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE status = 'PAID' ORDER BY createdAt DESC")
    fun observePaidSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE status = 'PAID'")
    suspend fun paidSales(): List<SaleEntity>

    @Upsert
    suspend fun upsert(sale: SaleEntity)
}
