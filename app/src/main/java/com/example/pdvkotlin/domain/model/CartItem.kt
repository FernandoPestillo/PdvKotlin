package com.example.pdvkotlin.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int,
    val discount: Double = 0.0,
    val notes: String = "",
) {
    val grossTotal: Double = product.salePrice * quantity
    val total: Double = (grossTotal - discount).coerceAtLeast(0.0)
}
