package com.example.pdvkotlin.domain.usecase

import com.example.pdvkotlin.domain.model.Sale
import com.example.pdvkotlin.domain.repository.SaleRepository
import javax.inject.Inject

class SaveSaleUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend operator fun invoke(sale: Sale) = repository.saveSale(sale)
}

class ObserveHeldSalesUseCase @Inject constructor(private val repository: SaleRepository) {
    operator fun invoke() = repository.observeHeldSales()
}

class ObservePaidSalesUseCase @Inject constructor(private val repository: SaleRepository) {
    operator fun invoke() = repository.observePaidSales()
}

class GetReportSummaryUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend operator fun invoke() = repository.reports()
}
