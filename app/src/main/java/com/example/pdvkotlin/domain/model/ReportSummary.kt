package com.example.pdvkotlin.domain.model

data class ReportSummary(
    val dailySalesTotal: Double,
    val salesCount: Int,
    val averageTicket: Double,
    val bestSellers: List<Pair<String, Int>>,
)
