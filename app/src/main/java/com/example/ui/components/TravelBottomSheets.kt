package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Destination
import com.example.model.DestinationCatalog
import com.example.model.GeneratedTravelPlan
import com.example.model.TravelVibe
import com.example.model.formatBudget
import com.example.ui.theme.AmberSoft
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CoralSoft
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamWarmTint
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.InkNavy
import com.example.ui.theme.InkNavyMuted
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.LavenderSoft
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageMint
import com.example.ui.theme.SageSoft
import com.example.ui.theme.SoftBorder
import com.example.ui.viewmodel.PlannerSheetState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BudgetPickerSheet(
    currentBudgetTnd: Int,
    currencyCode: String,
    onSelectBudget: (Int) -> Unit,
    onSelectCurrency: (String) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    var sliderValue by remember(currentBudgetTnd) { mutableFloatStateOf(currentBudgetTnd.toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Set your trip budget",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Flights, cozy stays, and local adventures combined",
                fontFamily = NunitoFontFamily,
                fontSize = 14.sp,
                color = InkNavyMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Currency pills
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DestinationCatalog.currencies.forEach { code ->
                    val active = code == currencyCode
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (active) InkNavy else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (active) InkNavy else SoftBorder, RoundedCornerShape(20.dp))
                            .clickable { onSelectCurrency(code) }
                    ) {
                        Text(
                            text = code,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (active) PureWhite else InkNavy,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = formatBudget(sliderValue.roundToInt(), currencyCode),
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = CoralRose
            )

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = ((it / 50f).roundToInt() * 50f) },
                valueRange = 500f..5000f,
                colors = SliderDefaults.colors(
                    thumbColor = CoralRose,
                    activeTrackColor = CoralRose,
                    inactiveTrackColor = SoftBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DestinationCatalog.budgetPresetsTnd.forEach { preset ->
                    val selected = sliderValue.roundToInt() == preset
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) CoralSoft else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                1.5.dp,
                                if (selected) CoralRose else SoftBorder,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { sliderValue = preset.toFloat() }
                    ) {
                        Text(
                            text = formatBudget(preset, currencyCode),
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selected) CoralRose else InkNavy,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
            Button(
                onClick = {
                    onSelectBudget(sliderValue.roundToInt())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("apply_budget_button")
            ) {
                Text(
                    text = "Apply budget",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PureWhite
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginPickerSheet(
    currentOrigin: String,
    onSelectOrigin: (String) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Where are you flying from?",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(14.dp))

            DestinationCatalog.origins.forEach { origin ->
                val selected = origin == currentOrigin
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) LavenderSoft else PureWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            1.5.dp,
                            if (selected) LavenderPin else SoftBorder,
                            RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            onSelectOrigin(origin)
                            onDismiss()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.FlightTakeoff,
                                contentDescription = null,
                                tint = if (selected) LavenderPin else InkNavyMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = origin,
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = InkNavy
                            )
                        }
                        if (selected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = LavenderPin,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibePickerSheet(
    currentVibe: TravelVibe,
    onSelectVibe: (TravelVibe) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Pick your travel vibe",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Destinations on the globe spotlight matching trips",
                fontFamily = NunitoFontFamily,
                fontSize = 14.sp,
                color = InkNavyMuted
            )
            Spacer(modifier = Modifier.height(14.dp))

            TravelVibe.entries.forEach { vibe ->
                val selected = vibe == currentVibe
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) vibe.accentColor.copy(alpha = 0.16f) else PureWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            1.5.dp,
                            if (selected) vibe.accentColor else SoftBorder,
                            RoundedCornerShape(18.dp)
                        )
                        .testTag("vibe_option_${vibe.name.lowercase()}")
                        .clickable {
                            onSelectVibe(vibe)
                            onDismiss()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(vibe.accentColor)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = vibe.shortTitle,
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = InkNavy
                                )
                                Text(
                                    text = vibe.subtitle,
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 12.sp,
                                    color = InkNavyMuted
                                )
                            }
                        }
                        if (selected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = vibe.accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailSheet(
    destination: Destination,
    isSaved: Boolean,
    currencyCode: String,
    onToggleFavorite: () -> Unit,
    onCraftPlan: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(22.dp))
            ) {
                DestinationArtwork(
                    artType = destination.artType,
                    contentDescription = "${destination.name}, ${destination.country}",
                    modifier = Modifier.matchParentSize()
                )
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PureWhite.copy(alpha = 0.92f))
                        .testTag("detail_favorite_button")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorite_toggle),
                        tint = if (isSaved) CoralRose else InkNavy
                    )
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PureWhite.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = destination.primaryVibe.shortTitle,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = InkNavy,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${destination.name}, ${destination.country}",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = InkNavy
                    )
                    Text(
                        text = destination.tagline,
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = InkNavyMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CoralSoft
                ) {
                    Text(
                        text = "~${formatBudget(destination.estimatedBudgetTnd, currencyCode)}",
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CoralRose,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoChipCard(
                    icon = Icons.Filled.Schedule,
                    label = "${destination.durationDays} days",
                    modifier = Modifier.weight(1f)
                )
                InfoChipCard(
                    icon = Icons.Filled.FlightTakeoff,
                    label = destination.flightTimeFromTunisia,
                    modifier = Modifier.weight(1.4f)
                )
                InfoChipCard(
                    icon = Icons.Filled.WbSunny,
                    label = destination.bestSeason,
                    modifier = Modifier.weight(1.2f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Must-do highlights",
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(8.dp))

            destination.highlights.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(destination.pinColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = item,
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = InkNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onCraftPlan,
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("craft_destination_plan_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = AmberSun,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Get plan for ${destination.name}",
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PureWhite
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoChipCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PureWhite,
        modifier = modifier.border(1.dp, SoftBorder, RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LavenderPin,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = InkNavy,
                maxLines = 2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPlannerResultsSheet(
    state: PlannerSheetState,
    currencyCode: String,
    onSelectPlanIndex: (Int) -> Unit,
    onSavePlan: (GeneratedTravelPlan) -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    if (state is PlannerSheetState.Hidden) return

    BackHandler(onBack = onDismiss)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        when (state) {
            is PlannerSheetState.Loading -> {
                val pulse by rememberInfiniteTransition(label = "loadingPulse").animateFloat(
                    initialValue = 0.88f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(700, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .scale(pulse)
                            .clip(CircleShape)
                            .background(InkNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = AmberSun,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Sketching your getaway...",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = InkNavy
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Matching routes, stays & local favorites to your budget",
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = InkNavyMuted
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            is PlannerSheetState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Couldn't craft plans right now",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = InkNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = InkNavyMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
                    ) {
                        Text("Try again", color = PureWhite)
                    }
                }
            }

            is PlannerSheetState.Results -> {
                val activePlan = state.plans.getOrElse(state.selectedIndex) { state.plans.first() }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = AmberSun,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Your Curated Plans",
                                fontFamily = FredokaFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = InkNavy
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.cd_close_sheet),
                                tint = InkNavy
                            )
                        }
                    }

                    // In-app notice required when Firebase AI Logic is not enabled
                    if (state.isSamplePreview) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = AmberSoft,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(1.dp, AmberSun.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = InkNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.sample_ai_notice),
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = InkNavy
                                )
                            }
                        }
                    }

                    // Destination switcher pills if multiple plans were generated
                    if (state.plans.size > 1) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.plans.forEachIndexed { idx, plan ->
                                val selected = idx == state.selectedIndex
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (selected) InkNavy else PureWhite,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(1.dp, if (selected) InkNavy else SoftBorder, RoundedCornerShape(20.dp))
                                        .clickable { onSelectPlanIndex(idx) }
                                ) {
                                    Text(
                                        text = plan.destinationName,
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (selected) PureWhite else InkNavy,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Summary header card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PureWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SoftBorder, RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${activePlan.destinationName}, ${activePlan.country}",
                                        fontFamily = FredokaFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = InkNavy
                                    )
                                    Text(
                                        text = "${activePlan.origin} • ${activePlan.vibeLabel}",
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = CoralRose
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = SageSoft
                                ) {
                                    Text(
                                        text = formatBudget(activePlan.budgetTnd, currencyCode),
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = InkNavy,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = activePlan.summary,
                                fontFamily = NunitoFontFamily,
                                fontSize = 13.sp,
                                color = InkNavyMuted
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            BudgetBreakdownRow(plan = activePlan, currencyCode = currencyCode)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Day-by-day itinerary",
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = InkNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    activePlan.days.forEach { day ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = PureWhite,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .border(1.dp, SoftBorder, RoundedCornerShape(18.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Day ${day.dayNumber}: ${day.title}",
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = InkNavy
                                    )
                                    Text(
                                        text = "~${formatBudget(day.estimatedCostTnd, currencyCode)}",
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = LavenderPin
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Morning: ${day.morning}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 13.sp,
                                    color = InkNavyMuted
                                )
                                Text(
                                    text = "Afternoon: ${day.afternoon}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 13.sp,
                                    color = InkNavyMuted
                                )
                                Text(
                                    text = "Evening: ${day.evening}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 13.sp,
                                    color = InkNavyMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CreamWarmTint,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Travel Tip: ${activePlan.localTip}",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = InkNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pack smart: ${activePlan.packingTip}",
                                fontFamily = NunitoFontFamily,
                                fontSize = 12.sp,
                                color = InkNavyMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Keep exploring",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = InkNavy
                            )
                        }
                        Button(
                            onClick = { onSavePlan(activePlan) },
                            colors = ButtonDefaults.buttonColors(containerColor = CoralRose),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(50.dp)
                                .testTag("save_plan_to_trips_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Luggage,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Save to Trips",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            PlannerSheetState.Hidden -> Unit
        }
    }
}

@Composable
fun BudgetBreakdownRow(
    plan: GeneratedTravelPlan,
    currencyCode: String
) {
    val total = (plan.flightEstimateTnd + plan.stayEstimateTnd + plan.foodAndActivitiesTnd).coerceAtLeast(1)
    val fWeight = (plan.flightEstimateTnd.toFloat() / total).coerceAtLeast(0.1f)
    val sWeight = (plan.stayEstimateTnd.toFloat() / total).coerceAtLeast(0.1f)
    val aWeight = (plan.foodAndActivitiesTnd.toFloat() / total).coerceAtLeast(0.1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Box(modifier = Modifier.weight(fWeight).height(8.dp).background(CoralRose))
            Box(modifier = Modifier.weight(sWeight).height(8.dp).background(LavenderPin))
            Box(modifier = Modifier.weight(aWeight).height(8.dp).background(SageMint))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem("Flights", formatBudget(plan.flightEstimateTnd, currencyCode), CoralRose)
            LegendItem("Stay", formatBudget(plan.stayEstimateTnd, currencyCode), LavenderPin)
            LegendItem("Food & Fun", formatBudget(plan.foodAndActivitiesTnd, currencyCode), SageMint)
        }
    }
}

@Composable
private fun LegendItem(label: String, amount: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                fontFamily = NunitoFontFamily,
                fontSize = 11.sp,
                color = InkNavyMuted
            )
            Text(
                text = amount,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = InkNavy
            )
        }
    }
}
