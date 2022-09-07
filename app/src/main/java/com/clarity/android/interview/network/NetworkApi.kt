package com.clarity.android.interview.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    @GET("orders")
    fun fetchOrdersObservable(): Observable<OrdersResponse>

    @GET("order/{order_id}")
    fun fetchOrderByIdObservable(@Path("order_id") id: Long): Observable<OrderResponse>

    @GET("discount/{item_id}")
    fun discountObservable(@Path("item_id") id: Long): Observable<DiscountResponse>

    @GET("orders")
    suspend fun fetchOrdersSuspendable(): OrdersResponse

    @GET("order/{order_id}")
    suspend fun fetchOrderByIdSuspendable(@Path("order_id") id: Long): OrderResponse

    @GET("discount/{item_id}")
    suspend fun discountSuspendable(@Path("item_id") id: Long): DiscountResponse
}