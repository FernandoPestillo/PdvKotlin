package com.example.pdvkotlin.domain.usecase

import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.domain.repository.ProductRepository
import javax.inject.Inject

class ObserveProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(query: String) = repository.observeProducts(query)
}

class SaveProductUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) = repository.upsert(product)
}

class FindProductByBarcodeUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(barcode: String) = repository.getByBarcode(barcode)
}

class SeedProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke() = repository.seedInitialProducts()
}
