package com.example.pdvkotlin.features.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdvkotlin.domain.model.Payment
import com.example.pdvkotlin.domain.model.PaymentMethod
import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.domain.model.Sale
import com.example.pdvkotlin.domain.model.SaleStatus
import com.example.pdvkotlin.domain.usecase.ObserveProductsUseCase
import com.example.pdvkotlin.domain.usecase.SaveSaleUseCase
import com.example.pdvkotlin.domain.usecase.SeedProductsUseCase
import com.example.pdvkotlin.features.cart.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class SaleViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val saveSaleUseCase: SaveSaleUseCase,
    observeProductsUseCase: ObserveProductsUseCase,
    seedProductsUseCase: SeedProductsUseCase,
) : ViewModel() {
    val query = MutableStateFlow("")
    val cart = cartManager.state
    val products: StateFlow<List<Product>> = query
        .debounce(150)
        .flatMapLatest { observeProductsUseCase(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { seedProductsUseCase() }
    }

    fun setQuery(value: String) {
        query.value = value
    }

    fun add(product: Product) = cartManager.add(product)
    fun remove(productId: String) = cartManager.remove(productId)
    fun changeQuantity(productId: String, quantity: Int) = cartManager.changeQuantity(productId, quantity)
    fun setDiscount(value: String) = cartManager.setDiscount(value.replace(",", ".").toDoubleOrNull() ?: 0.0)
    fun setNotes(value: String) = cartManager.setNotes(value)
    fun cancel() = cartManager.clear()

    fun holdSale() {
        val state = cart.value
        if (state.items.isEmpty()) return
        viewModelScope.launch {
            saveSaleUseCase(
                Sale(
                    id = UUID.randomUUID().toString(),
                    items = state.items,
                    discount = state.totalDiscount,
                    notes = state.notes,
                    status = SaleStatus.HELD,
                    payments = listOf(Payment(PaymentMethod.CASH, 0.0)),
                )
            )
            cartManager.clear()
        }
    }
}

