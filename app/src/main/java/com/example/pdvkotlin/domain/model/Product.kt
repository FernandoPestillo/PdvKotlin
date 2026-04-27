package com.example.pdvkotlin.domain.model

data class Product(
    val id: String,
    val name: String,
    val barcode: String,
    val category: String,
    val stock: Int,
    val costPrice: Double,
    val salePrice: Double,
    val isActive: Boolean = true,
)
