package com.example.pdvkotlin.features.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.domain.usecase.ObserveProductsUseCase
import com.example.pdvkotlin.domain.usecase.SaveProductUseCase
import com.example.pdvkotlin.domain.usecase.SeedProductsUseCase
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
class ProductsViewModel @Inject constructor(
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    seedProductsUseCase: SeedProductsUseCase,
) : ViewModel() {
    val query = MutableStateFlow("")

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

    fun addQuickProduct(name: String, priceText: String) {
        val price = priceText.replace(",", ".").toDoubleOrNull() ?: return
        viewModelScope.launch {
            saveProductUseCase(
                Product(
                    id = UUID.randomUUID().toString(),
                    name = name.ifBlank { "Produto sem nome" },
                    barcode = System.currentTimeMillis().toString(),
                    category = "Geral",
                    stock = 1,
                    costPrice = price * 0.6,
                    salePrice = price,
                )
            )
        }
    }
}

