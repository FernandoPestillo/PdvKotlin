package com.example.pdvkotlin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pdvkotlin.data.local.dao.AuditLogDao
import com.example.pdvkotlin.data.local.dao.ProductDao
import com.example.pdvkotlin.data.local.dao.SaleDao
import com.example.pdvkotlin.data.local.entity.AuditLogEntity
import com.example.pdvkotlin.data.local.entity.ProductEntity
import com.example.pdvkotlin.data.local.entity.SaleEntity

@Database(
    entities = [ProductEntity::class, SaleEntity::class, AuditLogEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PdvDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun auditLogDao(): AuditLogDao
}
