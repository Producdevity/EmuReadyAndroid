package com.emuready.emuready.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emuready.emuready.domain.entities.Listing
import com.emuready.emuready.domain.usecases.GetListingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListingsUiState())
    val uiState: StateFlow<ListingsUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow("All")
    private val sortOption = MutableStateFlow("Default")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val listings: Flow<PagingData<Listing>> = combine(
        searchQuery.debounce(300),
        selectedCategory,
        sortOption
    ) { query, category, sort ->
        ListingsQuery(query, category, sort)
    }.flatMapLatest { query ->
        getListingsUseCase(
            search = query.searchQuery.takeIf { it.isNotBlank() },
            gameId = null, // Will be set when user selects a specific game
            systemId = null,
            deviceId = null, // Will be set when user selects a specific device
            emulatorId = null // Will be set when user selects a specific emulator
        )
    }.cachedIn(viewModelScope)

    init {
        // Update UI state when search/category/sort change
        viewModelScope.launch {
            combine(
                searchQuery,
                selectedCategory,
                sortOption
            ) { query, category, sort ->
                ListingsUiState(
                    searchQuery = query,
                    selectedCategory = category,
                    sortOption = sort
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun updateSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    fun updateSortOption(option: String) {
        sortOption.value = option
    }
}

data class ListingsUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val sortOption: String = "Default"
)

private data class ListingsQuery(
    val searchQuery: String,
    val selectedCategory: String,
    val sortOption: String
)