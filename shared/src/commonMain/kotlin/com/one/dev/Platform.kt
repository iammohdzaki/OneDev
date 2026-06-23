package com.one.dev

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

// Platform-specific epoch time — mapped to Date.now() on JS/WASM
expect fun currentTimeMillis(): Long