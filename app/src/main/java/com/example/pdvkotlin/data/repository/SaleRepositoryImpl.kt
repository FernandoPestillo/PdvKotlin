package com.example.pdvkotlin.data.repository

import com.example.pdvkotlin.data.local.dao.SaleDao
import com.example.pdvkotlin.data.local.entity.SaleEntity
import com.example.pdvkotlin.domain.model.CartItem
import com.example.pdvkotlin.domain.model.Payment
import com.example.pdvkotlin.domain.model.PaymentMethod
import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.domain.model.ReportSummary
import com.example.pdvkotlin.domain.model.Sale
import com.example.pdvkotlin.domain.model.SaleStatus
import com.example.pdvkotlin.domain.repository.SaleRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepositoryImpl @Inject constructor(
    private val saleDao: SaleDao,
    private val json: Json,
) : SaleRepository {
    override fun observeHeldSales(): Flow<List<Sale>> =
        saleDao.observeHeldSales().map { sales -> sales.map { it.toDomain(json) } }

    override fun observePaidSales(): Flow<List<Sale>> =
        saleDao.observePaidSales().map { sales -> sales.map { it.toDomain(json) } }

    override suspend fun saveSale(sale: Sale) {
        saleDao.upsert(sale.toEntity(json))
    }

    override suspend fun reports(): ReportSummary {
        val paidSales = saleDao.paidSales().map { it.toDomain(json) }
        val total = paidSales.sumOf { it.total }
        val count = paidSales.size
        val bestSellers = paidSales
            .flatMap { it.items }
            .groupBy { it.product.name }
            .mapValues { entry -> entry.value.sumOf { it.quantity } }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

        return ReportSummary(
            dailySalesTotal = total,
            salesCount = count,
            averageTicket = if (count == 0) 0.0 else total / count,
            bestSellers = bestSellers,
        )
    }
}

@Serializable
private data class CartItemSnapshot(
    val product: ProductSnapshot,
    val quantity: Int,
    val discount: Double,
    val notes: String,
)

@Serializable
private data class ProductSnapshot(
    val id: String,
    val name: String,
    val barcode: String,
    val category: String,
    val stock: Int,
    val costPrice: Double,
    val salePrice: Double,
    val isActive: Boolean,
)

@Serializable
private data class PaymentSnapshot(
    val method: String,
    val amount: Double,
)

private fun Sale.toEntity(json: Json) = SaleEntity(
    id = id,
    status = status.name,
    discount = discount,
    notes = notes,
    subtotal = subtotal,
    total = total,
    paidAmount = paidAmount,
    change = change,
    itemSnapshotJson = json.encodeToString(items.map { it.toSnapshot() }),
    paymentSnapshotJson = json.encodeToString(payments.map { PaymentSnapshot(it.method.name, it.amount) }),
    createdAt = createdAt.toString(),
)

private fun SaleEntity.toDomain(json: Json): Sale {
    val items = json.decodeFromString<List<CartItemSnapshot>>(itemSnapshotJson).map { it.toDomain() }
    val payments = json.decodeFromString<List<PaymentSnapshot>>(paymentSnapshotJson).map {
        Payment(PaymentMethod.valueOf(it.method), it.amount)
    }
    return Sale(
        id = id,
        items = items,
        discount = discount,
        notes = notes,
        status = SaleStatus.valueOf(status),
        payments = payments,
        createdAt = LocalDateTime.parse(createdAt),
    )
}

private fun CartItem.toSnapshot() = CartItemSnapshot(
    product = ProductSnapshot(
        id = product.id,
        name = product.name,
        barcode = product.barcode,
        category = product.category,
        stock = product.stock,
        costPrice = product.costPrice,
        salePrice = product.salePrice,
        isActive = product.isActive,
    ),
    quantity = quantity,
    discount = discount,
    notes = notes,
)

private fun CartItemSnapshot.toDomain() = CartItem(
    product = Product(
        id = product.id,
        name = product.name,
        barcode = product.barcode,
        category = product.category,
        stock = product.stock,
        costPrice = product.costPrice,
        salePrice = product.salePrice,
        isActive = product.isActive,
    ),
    quantity = quantity,
    discount = discount,
    notes = notes,
)
