package com.example.pdvkotlin.data.local

import com.example.pdvkotlin.domain.model.Product

object SeedData {
    val products = listOf(
        Product("p1", "Cafe espresso", "7891000000011", "Bebidas", 120, 1.40, 5.50),
        Product("p2", "Pao de queijo", "7891000000028", "Lanches", 80, 1.10, 4.90),
        Product("p3", "Agua mineral 500ml", "7891000000035", "Bebidas", 150, 1.00, 3.50),
        Product("p4", "Sanduiche natural", "7891000000042", "Lanches", 35, 6.50, 14.90),
        Product("p5", "Chocolate barra", "7891000000059", "Conveniência", 90, 2.70, 6.90),
        Product("p6", "Marmita executiva", "7891000000066", "Restaurante", 24, 12.00, 29.90),
    )
}
