package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiPlanResult
import com.example.data.ai.SampleTravelPlanAiRepository
import com.example.data.ai.TravelPlanAiRepository
import com.example.data.local.TravelDatabase
import com.example.data.local.TravelPreferencesRepository
import com.example.data.local.TravelRepository
import com.example.model.AppTab
import com.example.model.Destination
import com.example.model.DestinationCatalog
import com.example.model.GeneratedTravelPlan
import com.example.model.TravelVibe
import com.example.model.TravelerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ActiveFilterSheet {
    NONE, BUDGET, ORIGIN, VIBE
}

sealed interface PlannerSheetState {
    data object Hidden : PlannerSheetState
    data object Loading : PlannerSheetState
    data class Results(
        val plans: List<GeneratedTravelPlan>,
        val selectedIndex: Int = 0,
        val isSamplePreview: Boolean = true
    ) : PlannerSheetState
    data class Error(val message: String) : PlannerSheetState
}

class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TravelDatabase.getInstance(application)
    private val travelRepository = TravelRepository(database.travelDao())
    private val prefsRepository = TravelPreferencesRepository(application)
    private val aiRepository: TravelPlanAiRepository = SampleTravelPlanAiRepository()

    val travelerProfile: StateFlow<TravelerProfile> = prefsRepository.profileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TravelerProfile()
    )

    val savedDestinationIds: StateFlow<Set<String>> = travelRepository.savedDestinationIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptySet()
    )

    val savedTrips: StateFlow<List<GeneratedTravelPlan>> = travelRepository.savedTrips.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedTab = MutableStateFlow(AppTab.PLAN)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _selectedDestination = MutableStateFlow<Destination?>(null)
    val selectedDestination: StateFlow<Destination?> = _selectedDestination.asStateFlow()

    private val _activeFilterSheet = MutableStateFlow(ActiveFilterSheet.NONE)
    val activeFilterSheet: StateFlow<ActiveFilterSheet> = _activeFilterSheet.asStateFlow()

    private val _plannerSheetState = MutableStateFlow<PlannerSheetState>(PlannerSheetState.Hidden)
    val plannerSheetState: StateFlow<PlannerSheetState> = _plannerSheetState.asStateFlow()

    private val _savedTripNotice = MutableStateFlow<String?>(null)
    val savedTripNotice: StateFlow<String?> = _savedTripNotice.asStateFlow()

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun openFilterSheet(sheet: ActiveFilterSheet) {
        _activeFilterSheet.value = sheet
    }

    fun closeFilterSheet() {
        _activeFilterSheet.value = ActiveFilterSheet.NONE
    }

    fun updateBudget(budgetTnd: Int) {
        viewModelScope.launch {
            prefsRepository.updateBudget(budgetTnd)
        }
    }

    fun updateOrigin(origin: String) {
        viewModelScope.launch {
            prefsRepository.updateOrigin(origin)
        }
    }

    fun updateVibe(vibe: TravelVibe) {
        viewModelScope.launch {
            prefsRepository.updateVibe(vibe)
        }
    }

    fun updateCurrency(currencyCode: String) {
        viewModelScope.launch {
            prefsRepository.updateCurrency(currencyCode)
        }
    }

    fun updateTravelerName(name: String) {
        viewModelScope.launch {
            prefsRepository.updateName(name)
        }
    }

    fun updateAvatarIndex(index: Int) {
        viewModelScope.launch {
            prefsRepository.updateAvatarIndex(index)
        }
    }

    fun selectDestination(destination: Destination?) {
        _selectedDestination.value = destination
    }

    fun toggleFavorite(destinationId: String) {
        val isSaved = savedDestinationIds.value.contains(destinationId)
        viewModelScope.launch {
            travelRepository.toggleSavedDestination(destinationId, isSaved)
        }
    }

    fun generateTravelPlans(focusDestination: Destination? = null) {
        val profile = travelerProfile.value
        _selectedDestination.value = null
        _plannerSheetState.value = PlannerSheetState.Loading

        viewModelScope.launch {
            when (
                val result = aiRepository.generatePlans(
                    origin = profile.homeOrigin,
                    budgetTnd = profile.defaultBudgetTnd,
                    vibe = profile.preferredVibe,
                    focusDestination = focusDestination
                )
            ) {
                is AiPlanResult.Success -> {
                    _plannerSheetState.value = PlannerSheetState.Results(
                        plans = result.plans,
                        selectedIndex = 0,
                        isSamplePreview = result.isSamplePreview
                    )
                }
                is AiPlanResult.Error -> {
                    _plannerSheetState.value = PlannerSheetState.Error(result.message)
                }
            }
        }
    }

    fun selectPlanResultTab(index: Int) {
        val current = _plannerSheetState.value
        if (current is PlannerSheetState.Results && index in current.plans.indices) {
            _plannerSheetState.value = current.copy(selectedIndex = index)
        }
    }

    fun savePlanToTrips(plan: GeneratedTravelPlan) {
        viewModelScope.launch {
            travelRepository.saveTrip(plan)
            _savedTripNotice.value = "${plan.destinationName} saved to Trips!"
            _plannerSheetState.value = PlannerSheetState.Hidden
            _selectedTab.value = AppTab.TRIPS
        }
    }

    fun deleteSavedTrip(tripId: String) {
        viewModelScope.launch {
            travelRepository.deleteTrip(tripId)
        }
    }

    fun dismissPlannerSheet() {
        _plannerSheetState.value = PlannerSheetState.Hidden
    }

    fun clearNotice() {
        _savedTripNotice.value = null
    }

    fun getMatchingDestinations(): List<Destination> {
        val profile = travelerProfile.value
        return DestinationCatalog.destinations.filter { it.matchesVibe(profile.preferredVibe) }
    }
}
