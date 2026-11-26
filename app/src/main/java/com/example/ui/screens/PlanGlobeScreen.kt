package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.Destination
import com.example.model.DestinationCatalog
import com.example.model.TravelerProfile
import com.example.ui.components.BottomGetPlansButton
import com.example.ui.components.FilterPillsRow
import com.example.ui.components.FloatingGetPlansPillRow
import com.example.ui.components.InteractiveTravelGlobeStage
import com.example.ui.components.TeeBrandHeader

@Composable
fun PlanGlobeScreen(
    profile: TravelerProfile,
    savedIds: Set<String>,
    onAvatarClick: () -> Unit,
    onBudgetClick: () -> Unit,
    onOriginClick: () -> Unit,
    onVibeClick: () -> Unit,
    onGetPlansClick: () -> Unit,
    onDestinationClick: (Destination) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TeeBrandHeader(
                avatarColorIndex = profile.avatarColorIndex,
                hasUnreadBadge = true,
                onAvatarClick = onAvatarClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            FilterPillsRow(
                budgetTnd = profile.defaultBudgetTnd,
                currencyCode = profile.currencyCode,
                origin = profile.homeOrigin,
                selectedVibe = profile.preferredVibe,
                onBudgetClick = onBudgetClick,
                onOriginClick = onOriginClick,
                onVibeClick = onVibeClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            FloatingGetPlansPillRow(onClick = onGetPlansClick)

            // Centerpiece interactive illustrated globe with Polaroids
            InteractiveTravelGlobeStage(
                destinations = DestinationCatalog.destinations,
                savedIds = savedIds,
                selectedVibe = profile.preferredVibe,
                budgetTnd = profile.defaultBudgetTnd,
                onDestinationClick = onDestinationClick,
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            BottomGetPlansButton(
                onClick = onGetPlansClick,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
