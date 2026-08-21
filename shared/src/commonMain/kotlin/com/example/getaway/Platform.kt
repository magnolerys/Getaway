package com.example.getaway

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform