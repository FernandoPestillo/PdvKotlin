package com.example.pdvkotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey val id: String,
    val status: String,
    val discount: Double,
    val notes: String,
    val subtotal: Double,
    val total: Double,
    val paidAmount: Double,
    val change: Double,
    val itemSnapshotJson: String,
    val paymentSnapshotJson: String,
    val createdAt: String,
    val pendingSync: Boolean = true,
)
