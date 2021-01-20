package com.example.termprojectuser

data class OrderList(
        val orderList: ArrayList<Order>,
        val queue: String,
        val user: User?
)
