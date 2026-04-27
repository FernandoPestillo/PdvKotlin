package com.example.pdvkotlin.core.navigation

sealed class PdvRoute(val route: String) {
    data object Splash : PdvRoute("splash")
    data object Login : PdvRoute("login")
    data object Dashboard : PdvRoute("dashboard")
    data object Products : PdvRoute("products")
    data object Sale : PdvRoute("sale")
    data object Cart : PdvRoute("cart")
    data object Payment : PdvRoute("payment")
    data object Receipt : PdvRoute("receipt")
    data object Reports : PdvRoute("reports")
    data object Settings : PdvRoute("settings")
}
