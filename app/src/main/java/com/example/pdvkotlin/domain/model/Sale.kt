package com.example.pdvkotlin.domain.model

import java.time.LocalDateTime

enum class SaleStatus { OPEN, HELD, PAID, CANCELED }
enum class PaymentMethod { CASH, CREDIT_CARD, DEBIT_CARD, PIX }

data class Payment(
    val method: PaymentMethod,
    val amount: Double,
)

data class Sale(
    val id: String,
    val items: List<CartItem>,
    val discount: Double,
    val notes: String,
    val status: SaleStatus,
    val payments: List<Payment> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    val subtotal: Double = items.sumOf { it.total }
    val total: Double = (subtotal - discount).coerceAtLeast(0.0)
    val paidAmount: Double = payments.sumOf { it.amount }
    val change: Double = (paidAmount - total).coerceAtLeast(0.0)
}
