package com.example.pdvkotlin.features.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdvkotlin.domain.model.Payment
import com.example.pdvkotlin.domain.model.PaymentMethod
import com.example.pdvkotlin.domain.model.Sale
import com.example.pdvkotlin.domain.model.SaleStatus
import com.example.pdvkotlin.domain.usecase.SaveSaleUseCase
import com.example.pdvkotlin.features.cart.CartManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentUiState(
    val cash: String = "",
    val credit: String = "",
    val debit: String = "",
    val pix: String = "",
    val saving: Boolean = false,
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val saveSaleUseCase: SaveSaleUseCase,
    private val receiptStore: ReceiptStore,
) : ViewModel() {
    val cart = cartManager.state
    private val _state = MutableStateFlow(PaymentUiState())
    val state: StateFlow<PaymentUiState> = _state

    fun setCash(value: String) = _state.update { it.copy(cash = value) }
    fun setCredit(value: String) = _state.update { it.copy(credit = value) }
    fun setDebit(value: String) = _state.update { it.copy(debit = value) }
    fun setPix(value: String) = _state.update { it.copy(pix = value) }

    fun finishSale(onDone: () -> Unit) {
        val cartState = cart.value
        if (cartState.items.isEmpty()) return
        viewModelScope.launch {
            _state.update { it.copy(saving = true) }
            val sale = Sale(
                id = UUID.randomUUID().toString(),
                items = cartState.items,
                discount = cartState.totalDiscount,
                notes = cartState.notes,
                status = SaleStatus.PAID,
                payments = buildPayments(),
            )
            saveSaleUseCase(sale)
            receiptStore.set(sale)
            cartManager.clear()
            _state.update { it.copy(saving = false) }
            onDone()
        }
    }

    private fun buildPayments(): List<Payment> {
        val current = state.value
        return listOfNotNull(
            current.cash.toPayment(PaymentMethod.CASH),
            current.credit.toPayment(PaymentMethod.CREDIT_CARD),
            current.debit.toPayment(PaymentMethod.DEBIT_CARD),
            current.pix.toPayment(PaymentMethod.PIX),
        )
    }

    private fun String.toPayment(method: PaymentMethod): Payment? {
        val amount = replace(",", ".").toDoubleOrNull() ?: return null
        return if (amount > 0.0) Payment(method, amount) else null
    }
}

