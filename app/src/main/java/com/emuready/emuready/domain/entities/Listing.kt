package com.emuready.emuready.domain.entities

data class Listing(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val seller: String,
    val sellerRating: Float,
    val condition: String, // "New", "Like New", "Good", "Fair", "Poor"
    val category: String, // "Devices", "Games", "Accessories", "Bundles"
    val imageUrl: String,
    val imageUrls: List<String> = emptyList(),
    val location: String,
    val isShippingAvailable: Boolean,
    val shippingCost: Double,
    val isFavorite: Boolean = false,
    val isNegotiable: Boolean = false,
    val views: Int = 0,
    val watchersCount: Int = 0,
    val createdAt: Long,
    val updatedAt: Long,
    val isActive: Boolean = true,
    val tags: List<String> = emptyList(),
    
    // Additional device-specific fields
    val deviceBrand: String? = null,
    val deviceModel: String? = null,
    val storageCapacity: String? = null,
    val color: String? = null,
    val accessories: List<String> = emptyList(),
    
    // Game-specific fields
    val platform: String? = null,
    val genre: String? = null,
    val isDigital: Boolean? = null,
    
    // Verification status
    val isVerified: Boolean = false,
    val verificationBadge: String? = null
)

data class ListingFilter(
    val category: String = "All",
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val condition: List<String> = emptyList(),
    val location: String? = null,
    val isShippingRequired: Boolean? = null,
    val sortBy: ListingSortBy = ListingSortBy.RECENT
)

enum class ListingSortBy {
    RECENT,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    MOST_POPULAR,
    MOST_WATCHED,
    SELLER_RATING
}

data class CreateMarketplaceListing(
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val condition: String = "",
    val category: String = "",
    val location: String = "",
    val isShippingAvailable: Boolean = false,
    val shippingCost: Double = 0.0,
    val isNegotiable: Boolean = false,
    val tags: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    
    // Device-specific fields
    val deviceBrand: String? = null,
    val deviceModel: String? = null,
    val storageCapacity: String? = null,
    val color: String? = null,
    val accessories: List<String> = emptyList(),
    
    // Game-specific fields
    val platform: String? = null,
    val genre: String? = null,
    val isDigital: Boolean? = null
) {
    fun isValid(): Boolean = listOf(
        title.isNotEmpty() && title.length >= 5,
        description.isNotEmpty() && description.length >= 20,
        price > 0,
        condition.isNotEmpty(),
        category.isNotEmpty(),
        location.isNotEmpty(),
        imageUrls.isNotEmpty()
    ).all { it }
    
    fun getValidationErrors(): List<String> {
        val errors = mutableListOf<String>()
        
        if (title.isEmpty() || title.length < 5) {
            errors.add("Title must be at least 5 characters long")
        }
        if (description.isEmpty() || description.length < 20) {
            errors.add("Description must be at least 20 characters long")
        }
        if (price <= 0) {
            errors.add("Price must be greater than 0")
        }
        if (condition.isEmpty()) {
            errors.add("Condition must be specified")
        }
        if (category.isEmpty()) {
            errors.add("Category must be selected")
        }
        if (location.isEmpty()) {
            errors.add("Location must be provided")
        }
        if (imageUrls.isEmpty()) {
            errors.add("At least one image is required")
        }
        
        return errors
    }
}