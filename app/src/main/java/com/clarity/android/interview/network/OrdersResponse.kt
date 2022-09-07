package com.clarity.android.interview.network

import com.google.gson.annotations.SerializedName

class OrdersResponse(
  @SerializedName("orders") val orders: List<Long>
)
