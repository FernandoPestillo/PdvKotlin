package com.example.pdvkotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val barcode: String,
    val category: String,
    val stock: Int,
    val costPrice: Double,
    val salePrice: Double,
    val isActive: Boolean,
    val updatedAt: Long,
    val pendingSync: Boolean = true,
)
