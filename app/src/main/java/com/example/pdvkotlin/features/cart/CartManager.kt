package com.example.pdvkotlin.features.cart

import com.example.pdvkotlin.domain.model.CartItem
import com.example.pdvkotlin.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class CartState(
    val items: List<CartItem> = emptyList(),
    val totalDiscount: Double = 0.0,
    val notes: String = "",
) {
    val subtotal: Double = items.sumOf { it.total }
    val total: Double = (subtotal - totalDiscount).coerceAtLeast(0.0)
    val itemCount: Int = items.sumOf { it.quantity }
}

@Singleton
class CartManager @Inject constructor() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    fun add(product: Product) {
        _state.update { state ->
            val current = state.items.firstOrNull { it.product.id == product.id }
            val nextItems = if (current == null) {
                state.items + CartItem(product, 1)
            } else {
                state.items.map { if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it }
            }
            state.copy(items = nextItems)
        }
    }

    fun changeQuantity(productId: String, quantity: Int) {
        _state.update { state ->
            state.copy(items = state.items.mapNotNull {
                when {
                    it.product.id != productId -> it
                    quantity <= 0 -> null
                    else -> it.copy(quantity = quantity)
                }
            })
        }
    }

    fun remove(productId: String) {
        _state.update { state -> state.copy(items = state.items.filterNot { it.product.id == productId }) }
    }

    fun setDiscount(value: Double) {
        _state.update { it.copy(totalDiscount = value.coerceAtLeast(0.0)) }
    }

    fun setNotes(value: String) {
        _state.update { it.copy(notes = value) }
    }

    fun clear() {
        _state.value = CartState()
    }
}
