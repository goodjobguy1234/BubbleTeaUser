package com.example.termprojectuser.Entity

data class Queue(
        val orderList: Map<String, OrderList> = mapOf(),
        val queueId: String = "Unknown"
)
