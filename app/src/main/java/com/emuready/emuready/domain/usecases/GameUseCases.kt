package com.emuready.emuready.domain.usecases

import androidx.paging.PagingData
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.entities.GameDetail
import com.emuready.emuready.domain.entities.GameSortOption
import com.emuready.emuready.domain.repositories.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(
        search: String? = null,
        genre: String? = null,
        sortBy: GameSortOption = GameSortOption.TITLE
    ): Flow<PagingData<Game>> {
        return gameRepository.getGames(search, genre, sortBy)
    }
}

class GetGameDetailUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: String): Result<GameDetail> {
        return gameRepository.getGameDetail(gameId)
    }
}

class GetFavoriteGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<List<Game>> {
        return gameRepository.getFavoriteGames()
    }
}

class GetRecentGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<Game>> {
        return gameRepository.getRecentGames(limit)
    }
}

class GetFeaturedGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(): Result<List<Game>> {
        return gameRepository.getFeaturedGames()
    }
}

class GetRecommendationsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Game>> {
        return gameRepository.getRecommendations(userId)
    }
}

class ToggleFavoriteGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: String): Result<Unit> {
        return gameRepository.toggleFavorite(gameId)
    }
}