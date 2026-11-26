package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppTab
import com.example.ui.components.AiPlannerResultsSheet
import com.example.ui.components.BudgetPickerSheet
import com.example.ui.components.DestinationDetailSheet
import com.example.ui.components.OriginPickerSheet
import com.example.ui.components.TeeBottomNavigationBar
import com.example.ui.components.VibePickerSheet
import com.example.ui.screens.PlanGlobeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedDestinationsScreen
import com.example.ui.screens.TripsScreen
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.CreamWarmTint
import com.example.ui.theme.TeeTravelTheme
import com.example.ui.viewmodel.ActiveFilterSheet
import com.example.ui.viewmodel.TravelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                CreamBackground.toArgb(),
                CreamBackground.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.WHITE,
                android.graphics.Color.WHITE
            )
        )
        setContent {
            TeeTravelTheme {
                TeeTravelApp()
            }
        }
    }
}

@Composable
fun TeeTravelApp(
    viewModel: TravelViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val profile by viewModel.travelerProfile.collectAsStateWithLifecycle()
    val savedIds by viewModel.savedDestinationIds.collectAsStateWithLifecycle()
    val savedTrips by viewModel.savedTrips.collectAsStateWithLifecycle()
    val selectedDestination by viewModel.selectedDestination.collectAsStateWithLifecycle()
    val activeFilterSheet by viewModel.activeFilterSheet.collectAsStateWithLifecycle()
    val plannerSheetState by viewModel.plannerSheetState.collectAsStateWithLifecycle()
    val savedTripNotice by viewModel.savedTripNotice.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(savedTripNotice) {
        val msg = savedTripNotice
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotice()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CreamSurface, CreamBackground, CreamWarmTint.copy(alpha = 0.5f))
                )
            )
    ) {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                TeeBottomNavigationBar(
                    selectedTab = selectedTab,
                    savedCount = savedIds.size,
                    tripsCount = savedTrips.size,
                    onSelectTab = viewModel::selectTab
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        val direction = if (targetState.ordinal >= initialState.ordinal) 1 else -1
                        (fadeIn(animationSpec = tween(220)) +
                            slideInHorizontally(animationSpec = tween(220)) { it / 8 * direction })
                            .togetherWith(
                                fadeOut(animationSpec = tween(180)) +
                                    slideOutHorizontally(animationSpec = tween(180)) { -it / 8 * direction }
                            )
                    },
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        AppTab.PLAN -> {
                            PlanGlobeScreen(
                                profile = profile,
                                savedIds = savedIds,
                                onAvatarClick = { viewModel.selectTab(AppTab.PROFILE) },
                                onBudgetClick = { viewModel.openFilterSheet(ActiveFilterSheet.BUDGET) },
                                onOriginClick = { viewModel.openFilterSheet(ActiveFilterSheet.ORIGIN) },
                                onVibeClick = { viewModel.openFilterSheet(ActiveFilterSheet.VIBE) },
                                onGetPlansClick = { viewModel.generateTravelPlans() },
                                onDestinationClick = viewModel::selectDestination,
                                onToggleFavorite = viewModel::toggleFavorite
                            )
                        }

                        AppTab.SAVED -> {
                            SavedDestinationsScreen(
                                savedIds = savedIds,
                                currencyCode = profile.currencyCode,
                                onDestinationClick = viewModel::selectDestination,
                                onToggleFavorite = viewModel::toggleFavorite,
                                onExploreMapClick = { viewModel.selectTab(AppTab.PLAN) },
                                onBackToPlan = { viewModel.selectTab(AppTab.PLAN) }
                            )
                        }

                        AppTab.TRIPS -> {
                            TripsScreen(
                                trips = savedTrips,
                                currencyCode = profile.currencyCode,
                                onDeleteTrip = viewModel::deleteSavedTrip,
                                onPlanNewTripClick = { viewModel.generateTravelPlans() },
                                onBackToPlan = { viewModel.selectTab(AppTab.PLAN) }
                            )
                        }

                        AppTab.PROFILE -> {
                            ProfileScreen(
                                profile = profile,
                                savedCount = savedIds.size,
                                tripsCount = savedTrips.size,
                                onUpdateName = viewModel::updateTravelerName,
                                onSelectAvatarColor = viewModel::updateAvatarIndex,
                                onSelectOrigin = viewModel::updateOrigin,
                                onSelectCurrency = viewModel::updateCurrency,
                                onSelectVibe = viewModel::updateVibe,
                                onBackToPlan = { viewModel.selectTab(AppTab.PLAN) }
                            )
                        }
                    }
                }
            }
        }

        // Filter Picker Sheets
        when (activeFilterSheet) {
            ActiveFilterSheet.BUDGET -> {
                BudgetPickerSheet(
                    currentBudgetTnd = profile.defaultBudgetTnd,
                    currencyCode = profile.currencyCode,
                    onSelectBudget = viewModel::updateBudget,
                    onSelectCurrency = viewModel::updateCurrency,
                    onDismiss = viewModel::closeFilterSheet
                )
            }

            ActiveFilterSheet.ORIGIN -> {
                OriginPickerSheet(
                    currentOrigin = profile.homeOrigin,
                    onSelectOrigin = viewModel::updateOrigin,
                    onDismiss = viewModel::closeFilterSheet
                )
            }

            ActiveFilterSheet.VIBE -> {
                VibePickerSheet(
                    currentVibe = profile.preferredVibe,
                    onSelectVibe = viewModel::updateVibe,
                    onDismiss = viewModel::closeFilterSheet
                )
            }

            ActiveFilterSheet.NONE -> Unit
        }

        // Polaroid Destination Detail Sheet
        selectedDestination?.let { destination ->
            DestinationDetailSheet(
                destination = destination,
                isSaved = savedIds.contains(destination.id),
                currencyCode = profile.currencyCode,
                onToggleFavorite = { viewModel.toggleFavorite(destination.id) },
                onCraftPlan = { viewModel.generateTravelPlans(focusDestination = destination) },
                onDismiss = { viewModel.selectDestination(null) }
            )
        }

        // AI Travel Planner Results Sheet
        AiPlannerResultsSheet(
            state = plannerSheetState,
            currencyCode = profile.currencyCode,
            onSelectPlanIndex = viewModel::selectPlanResultTab,
            onSavePlan = viewModel::savePlanToTrips,
            onRetry = { viewModel.generateTravelPlans() },
            onDismiss = viewModel::dismissPlannerSheet
        )
    }
}
