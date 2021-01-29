package com.example.termprojectuser

data class OrderList(
        val orderList: ArrayList<Order>, // TODO default value?
        val queue: String,
        val user: User?
)
