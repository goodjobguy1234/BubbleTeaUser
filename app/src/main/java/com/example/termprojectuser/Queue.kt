package com.example.termprojectuser

data class Queue(
    val orderList: Map<String, OrderList> = mapOf(),
    val queueId: String = "Unknown"
)
