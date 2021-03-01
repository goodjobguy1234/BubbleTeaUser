package com.example.termprojectuser.Entity

data class Sale(
        val imageUrl: String = "",
        val name: String = "Unknown",
        var price: Int = -1,
        val quantity: Int = -1
)
