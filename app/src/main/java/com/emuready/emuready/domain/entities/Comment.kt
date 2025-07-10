package com.emuready.emuready.domain.entities

/**
 * Comment entity for community discussions
 */
data class Comment(
    val id: String,
    val content: String,
    val authorId: String,
    val authorName: String,
    val listingId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val likeCount: Int = 0,
    val isVerified: Boolean = false
) {
    companion object {
        fun empty() = Comment(
            id = "",
            content = "",
            authorId = "",
            authorName = "",
            listingId = "",
            createdAt = java.lang.System.currentTimeMillis(),
            updatedAt = java.lang.System.currentTimeMillis()
        )
    }
}