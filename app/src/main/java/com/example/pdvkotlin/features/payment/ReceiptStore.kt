package com.example.pdvkotlin.features.payment

import com.example.pdvkotlin.domain.model.Sale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptStore @Inject constructor() {
    private val _sale = MutableStateFlow<Sale?>(null)
    val sale: StateFlow<Sale?> = _sale

    fun set(sale: Sale) {
        _sale.value = sale
    }
}
