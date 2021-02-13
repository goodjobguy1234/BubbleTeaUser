package com.example.termprojectuser.Entity

data class OrderList(
        val imageUrl: String = "",
        val isReward: Boolean = false,
        val name: String = "Unknown",
        val price: Int = -1,
        val quantity: Int = -1
)
