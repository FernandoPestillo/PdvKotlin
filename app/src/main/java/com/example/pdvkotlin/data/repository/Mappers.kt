package com.example.pdvkotlin.data.repository

import com.example.pdvkotlin.data.local.entity.ProductEntity
import com.example.pdvkotlin.domain.model.Product

fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    barcode = barcode,
    category = category,
    stock = stock,
    costPrice = costPrice,
    salePrice = salePrice,
    isActive = isActive,
)

fun Product.toEntity(now: Long = System.currentTimeMillis()) = ProductEntity(
    id = id,
    name = name,
    barcode = barcode,
    category = category,
    stock = stock,
    costPrice = costPrice,
    salePrice = salePrice,
    isActive = isActive,
    updatedAt = now,
)
