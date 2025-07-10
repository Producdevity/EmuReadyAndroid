package com.emuready.emuready.data.mappers

import com.emuready.emuready.data.local.entities.GameEntity
import com.emuready.emuready.data.remote.dto.TrpcResponseDtos
import com.emuready.emuready.domain.entities.*
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Mappers for Game-related DTOs to Domain entities
 * Following the exact specification requirements
 */

fun TrpcResponseDtos.MobileGame.toDomain(): Game = Game(
    id = id,
    title = title,
    coverImageUrl = imageUrl,
    boxartUrl = boxartUrl,
    bannerUrl = bannerUrl,
    system = system.toDomain(),
    averageCompatibility = 0f, // Will be calculated from listings
    totalMobileListings = _count.listings,
    totalPcListings = 0, // Will be filled from PC listings
    lastUpdated = Instant.now().epochSecond,
    isFavorite = false
)

fun TrpcResponseDtos.MobileSystem.toDomain(): System = System(
    id = id,
    name = name,
    manufacturer = null, // Not provided in mobile API
    generation = null, // Not provided in mobile API
    releaseYear = null // Not provided in mobile API
)

fun TrpcResponseDtos.MobileListing.toDomain(): Listing = Listing(
    id = id,
    gameId = game.id,
    game = game.toDomain(),
    type = ListingType.MOBILE_DEVICE,
    title = "${game.title} - ${device.modelName}",
    description = notes,
    deviceId = device.id,
    emulatorId = emulator.id,
    emulatorConfig = customFieldValues?.joinToString("\n") { "${it.customFieldDefinition.name}: ${it.value}" },
    performanceLevel = PerformanceLevel.fromValue(performance.rank),
    overallRating = successRate,
    framerate = 60f, // Default, will be enhanced when available
    stabilityRating = successRate,
    audioRating = successRate,
    visualRating = successRate,
    customFields = customFieldValues?.associate {
        it.customFieldDefinition.id to it.value
    } ?: emptyMap(),
    userId = author.id,
    username = author.name ?: "Anonymous",
    isVerified = false, // Will be determined by verification system
    likeCount = _count.votes, // Mapped from votes
    dislikeCount = 0, // Not provided in current API
    commentCount = _count.comments,
    createdAt = parseIsoDate(createdAt),
    updatedAt = parseIsoDate(updatedAt)
)

fun TrpcResponseDtos.MobilePcListing.toDomain(): PcListing = PcListing(
    id = id,
    gameId = game.id,
    userId = author.id,
    cpuId = cpu.id,
    gpuId = gpu.id,
    emulatorId = emulator.id,
    memorySize = memorySize,
    performanceId = performance.id.toString(),
    description = notes ?: "",
    screenshotUrls = emptyList(), // Not provided in current API
    customFieldValues = customFieldValues?.associate {
        it.customFieldDefinition.id to it.value
    } ?: emptyMap(),
    approvalStatus = when (status) {
        TrpcResponseDtos.ApprovalStatus.PENDING -> ApprovalStatus.PENDING
        TrpcResponseDtos.ApprovalStatus.APPROVED -> ApprovalStatus.APPROVED
        TrpcResponseDtos.ApprovalStatus.REJECTED -> ApprovalStatus.REJECTED
    },
    isVerified = false, // Will be determined by verification system
    score = 0, // Will be calculated from votes
    totalVotes = _count.votes,
    createdAt = parseIsoDate(createdAt),
    updatedAt = parseIsoDate(updatedAt)
)

fun TrpcResponseDtos.MobileDevice.toDomain(): Device = Device(
    id = id,
    name = modelName,
    manufacturer = brand.name,
    model = modelName,
    chipset = soc?.name ?: "",
    gpu = soc?.name ?: "", // SoC often includes GPU info
    ramSize = 0, // Not provided in mobile API
    storageSize = 0, // Not provided in mobile API
    screenSize = 0f, // Not provided in mobile API
    screenResolution = "", // Not provided in mobile API
    operatingSystem = "", // Not provided in mobile API
    isVerified = false,
    benchmarkScore = null,
    registeredAt = Instant.now()
)

fun TrpcResponseDtos.MobileEmulator.toDomain(): Emulator = Emulator(
    id = id,
    name = name,
    logoUrl = logo,
    supportedSystems = systems?.map { it.toDomain() } ?: emptyList(),
    customFields = emptyList() // Will be loaded separately
)

fun TrpcResponseDtos.MobilePerformance.toDomain(): PerformanceLevel = when (rank) {
    1 -> PerformanceLevel.UNPLAYABLE
    2 -> PerformanceLevel.POOR
    3 -> PerformanceLevel.PLAYABLE
    4 -> PerformanceLevel.GOOD
    5 -> PerformanceLevel.GREAT
    6 -> PerformanceLevel.PERFECT
    else -> PerformanceLevel.PLAYABLE
}

fun TrpcResponseDtos.MobileComment.toDomain(): Comment = Comment(
    id = id,
    content = content,
    authorId = author.id,
    authorName = author.name ?: "Unknown",
    listingId = "", // Not directly provided, needs to be passed from context
    createdAt = parseIsoDate(createdAt),
    updatedAt = parseIsoDate(updatedAt),
    likeCount = _count.votes,
    isVerified = false // Not provided in current API
)

fun TrpcResponseDtos.MobileCustomFieldDefinition.toDomain(): CustomFieldDefinition = CustomFieldDefinition(
    id = id,
    name = name,
    label = label,
    type = CustomFieldType.TEXT, // TODO: Fix when CustomFieldType enum is available in DTOs
    isRequired = isRequired,
    options = options // Use the options string directly
)

fun TrpcResponseDtos.MobileCpu.toDomain(): CpuComponent = CpuComponent(
    id = id,
    brand = when (brand.name.lowercase()) {
        "intel" -> ComponentBrand.INTEL
        "amd" -> ComponentBrand.AMD
        else -> ComponentBrand.OTHER
    },
    modelName = name,
    coreCount = 0, // Not provided in mobile API
    baseClockSpeed = 0f // Not provided in mobile API
)

fun TrpcResponseDtos.MobileGpu.toDomain(): GpuComponent = GpuComponent(
    id = id,
    brand = when (brand.name.lowercase()) {
        "nvidia" -> ComponentBrand.NVIDIA
        "amd" -> ComponentBrand.AMD
        "intel" -> ComponentBrand.INTEL
        else -> ComponentBrand.OTHER
    },
    modelName = name,
    vramSize = memorySize ?: 0,
    architecture = null // Not provided in mobile API
)

// Helper function to parse ISO date strings
private fun parseIsoDate(isoString: String): Long {
    return try {
        Instant.from(DateTimeFormatter.ISO_INSTANT.parse(isoString)).epochSecond
    } catch (e: Exception) {
        Instant.now().epochSecond
    }
}

// Entity mappers (existing functionality preserved)
fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        boxartUrl = boxartUrl,
        bannerUrl = bannerUrl,
        systemId = system.id,
        systemName = system.name,
        averageCompatibility = averageCompatibility,
        totalMobileListings = totalMobileListings,
        totalPcListings = totalPcListings,
        lastUpdated = lastUpdated,
        isFavorite = isFavorite
    )
}

fun GameEntity.toDomain(): Game {
    return Game(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        boxartUrl = boxartUrl,
        bannerUrl = bannerUrl,
        system = System(id = systemId, name = systemName, manufacturer = null, generation = null, releaseYear = null),
        averageCompatibility = averageCompatibility,
        totalMobileListings = totalMobileListings,
        totalPcListings = totalPcListings,
        lastUpdated = lastUpdated,
        isFavorite = isFavorite
    )
}