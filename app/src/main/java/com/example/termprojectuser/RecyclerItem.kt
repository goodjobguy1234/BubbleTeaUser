package com.example.termprojectuser

sealed class RecyclerItem {
    data class Header(val typeName: String): RecyclerItem()
    data class Product(val order:Order): RecyclerItem()
}