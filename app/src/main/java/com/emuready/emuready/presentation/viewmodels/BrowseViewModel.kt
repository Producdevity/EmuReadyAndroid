package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emuready.emuready.domain.entities.Game
import com.emuready.emuready.domain.usecases.GetFilteredGamesUseCase
import com.emuready.emuready.domain.usecases.GetFiltersUseCase
import com.emuready.emuready.presentation.ui.screens.FilterOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getFilteredGamesUseCase: GetFilteredGamesUseCase,
    private val getFiltersUseCase: GetFiltersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(SortOption.DEFAULT)
    private val activeFilters = MutableStateFlow<Map<FilterType, Set<String>>>(emptyMap())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val games: Flow<PagingData<Game>> = combine(
        searchQuery.debounce(300),
        sortOption,
        activeFilters
    ) { query, sort, filters ->
        GameQuery(query, sort, filters)
    }.flatMapLatest { query ->
        getFilteredGamesUseCase(
            search = query.searchQuery.takeIf { it.isNotBlank() },
            sortBy = query.sortOption,
            systemIds = query.filters[FilterType.SYSTEM] ?: emptySet(),
            deviceIds = query.filters[FilterType.DEVICE] ?: emptySet(),
            emulatorIds = query.filters[FilterType.EMULATOR] ?: emptySet(),
            performanceIds = query.filters[FilterType.PERFORMANCE] ?: emptySet()
        )
    }.cachedIn(viewModelScope)

    init {
        loadAvailableFilters()

        // Update UI state when search/sort/filters change
        viewModelScope.launch {
            combine(
                searchQuery,
                sortOption,
                activeFilters
            ) { query, sort, filters ->
                BrowseUiState(
                    searchQuery = query,
                    sortOption = sort,
                    activeFilters = filters,
                    availableFilters = _uiState.value.availableFilters
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun updateSortOption(option: SortOption) {
        sortOption.value = option
    }

    fun toggleFilter(filterType: FilterType, optionId: String) {
        val currentFilters = activeFilters.value.toMutableMap()
        val typeFilters = currentFilters[filterType]?.toMutableSet() ?: mutableSetOf()

        if (optionId in typeFilters) {
            typeFilters.remove(optionId)
        } else {
            typeFilters.add(optionId)
        }

        if (typeFilters.isEmpty()) {
            currentFilters.remove(filterType)
        } else {
            currentFilters[filterType] = typeFilters
        }

        activeFilters.value = currentFilters
    }

    fun clearFilters() {
        activeFilters.value = emptyMap()
    }

    private fun loadAvailableFilters() {
        viewModelScope.launch {
            try {
                val filters = getFiltersUseCase()
                _uiState.update { state ->
                    state.copy(availableFilters = filters)
                }
            } catch (e: Exception) {
                // TODO: Handle error - could show a snackbar or log
            }
        }
    }
}

data class BrowseUiState(
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.DEFAULT,
    val activeFilters: Map<FilterType, Set<String>> = emptyMap(),
    val availableFilters: Map<FilterType, List<FilterOption>> = emptyMap()
)

enum class SortOption(val displayName: String) {
    DEFAULT("Default"),
    ALPHABETICAL("A-Z"),
    RECENT("Recently Added")
}

enum class FilterType(val displayName: String) {
    SYSTEM("System"),
    DEVICE("Device"),
    EMULATOR("Emulator"),
    PERFORMANCE("Performance")
}

private data class GameQuery(
    val searchQuery: String,
    val sortOption: SortOption,
    val filters: Map<FilterType, Set<String>>
)
