package com.emuready.emuready.domain.entities

data class System(
    val id: String,
    val name: String,
    val key: String,
    val description: String,
    val logoUrl: String,
    val manufacturer: String,
    val releaseYear: Int?
)