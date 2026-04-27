package com.example.pdvkotlin.core.ui

import java.text.NumberFormat
import java.util.Locale

fun Double.toMoney(): String = NumberFormat
    .getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
    .format(this)
