package com.example.retrofit500

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderService {

    @GET("order200")
    suspend fun getOrder200(): Response<Order>

    @GET("/")
    suspend fun getOrder500(): Response<Order>

    @POST("/")
    suspend fun postOrder500(): Response<Order>
}
