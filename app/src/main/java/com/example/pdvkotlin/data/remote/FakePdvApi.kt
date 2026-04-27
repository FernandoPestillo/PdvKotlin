package com.example.pdvkotlin.data.remote

import com.example.pdvkotlin.data.remote.dto.ProductDto
import retrofit2.http.GET

interface FakePdvApi {
    @GET("products")
    suspend fun products(): List<ProductDto>
}
