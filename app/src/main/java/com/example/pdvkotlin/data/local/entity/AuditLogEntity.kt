package com.example.pdvkotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val action: String,
    val details: String,
    val createdAt: Long,
)
