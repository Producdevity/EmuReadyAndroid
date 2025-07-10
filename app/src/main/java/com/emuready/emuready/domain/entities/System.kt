package com.emuready.emuready.domain.entities

/**
 * Gaming system/platform (e.g., Nintendo Switch, PlayStation, etc.)
 */
data class System(
    val id: String,
    val name: String,
    val manufacturer: String?,
    val generation: String?,
    val releaseYear: Int?
) {
    companion object {
        fun empty() = System(
            id = "",
            name = "",
            manufacturer = null,
            generation = null,
            releaseYear = null
        )
    }
}