package com.emuready.emuready.domain.entities

data class Emulator(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val supportedSystems: List<GameSystem> = emptyList()
)

data class GameSystem(
    val id: String,
    val name: String,
    val key: String
)