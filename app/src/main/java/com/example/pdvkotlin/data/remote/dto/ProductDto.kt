package com.example.pdvkotlin.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val barcode: String,
    val category: String,
    val stock: Int,
    val costPrice: Double,
    val salePrice: Double,
)
