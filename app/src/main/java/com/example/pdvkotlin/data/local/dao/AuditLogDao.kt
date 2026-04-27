package com.example.pdvkotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pdvkotlin.data.local.entity.AuditLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {
    @Insert
    suspend fun insert(log: AuditLogEntity)

    @Query("SELECT * FROM audit_logs ORDER BY createdAt DESC LIMIT 100")
    fun observeRecent(): Flow<List<AuditLogEntity>>
}
