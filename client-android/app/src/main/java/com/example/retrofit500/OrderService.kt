package com.example.retrofit500

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderService {

    @GET("order200")
    suspend fun getOrder200(): Response<Order>

    @GET("order500")
    suspend fun getOrder500(): Response<Order>

    @POST("order500")
    suspend fun postOrder500(): Response<Order>
}
