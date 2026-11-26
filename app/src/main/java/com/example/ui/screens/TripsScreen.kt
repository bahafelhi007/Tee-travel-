package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DestinationCatalog
import com.example.model.GeneratedTravelPlan
import com.example.model.formatBudget
import com.example.ui.components.BudgetBreakdownRow
import com.example.ui.components.DestinationArtwork
import com.example.ui.theme.AmberSoft
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CoralSoft
import com.example.ui.theme.CreamWarmTint
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.InkNavy
import com.example.ui.theme.InkNavyMuted
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageSoft
import com.example.ui.theme.SoftBorder

@Composable
fun TripsScreen(
    trips: List<GeneratedTravelPlan>,
    currencyCode: String,
    onDeleteTrip: (String) -> Unit,
    onPlanNewTripClick: () -> Unit,
    onBackToPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToPlan)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Trips",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = InkNavy
                    )
                    Text(
                        text = if (trips.isEmpty()) "Your saved itineraries live here"
                        else "${trips.size} planned trip${if (trips.size == 1) "" else "s"} ready to pack",
                        fontFamily = NunitoFontFamily,
                        fontSize = 14.sp,
                        color = InkNavyMuted
                    )
                }

                if (trips.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = InkNavy,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onPlanNewTripClick)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = AmberSun,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "New plan",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PureWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (trips.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(SageSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Luggage,
                                contentDescription = null,
                                tint = InkNavy,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.trips_empty_title),
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = InkNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.trips_empty_subtitle),
                            fontFamily = NunitoFontFamily,
                            fontSize = 14.sp,
                            color = InkNavyMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onPlanNewTripClick,
                            colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = AmberSun,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Get my plans",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(trips, key = { it.id }) { trip ->
                        TripItineraryCard(
                            trip = trip,
                            currencyCode = currencyCode,
                            onDelete = { onDeleteTrip(trip.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripItineraryCard(
    trip: GeneratedTravelPlan,
    currencyCode: String,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val destination = remember(trip.destinationId) { DestinationCatalog.findById(trip.destinationId) }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = PureWhite,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = InkNavy.copy(alpha = 0.12f))
            .clip(RoundedCornerShape(22.dp))
            .testTag("trip_card_${trip.id}")
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (destination != null) {
                    DestinationArtwork(
                        artType = destination.artType,
                        contentDescription = "${trip.destinationName}, ${trip.country}",
                        modifier = Modifier
                            .size(66.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${trip.destinationName}, ${trip.country}",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = InkNavy
                    )
                    Text(
                        text = "${trip.origin} • ${trip.days.size} days • ${trip.vibeLabel}",
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = CoralRose
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CoralSoft
                    ) {
                        Text(
                            text = formatBudget(trip.budgetTnd, currencyCode),
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = CoralRose,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = "Delete trip",
                        tint = InkNavyMuted
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse itinerary" else "Expand itinerary",
                    tint = InkNavy
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = SoftBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (trip.isSamplePreview) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AmberSoft,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = InkNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.sample_ai_notice),
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 11.sp,
                                    color = InkNavy
                                )
                            }
                        }
                    }

                    BudgetBreakdownRow(plan = trip, currencyCode = currencyCode)

                    Spacer(modifier = Modifier.height(12.dp))
                    trip.days.forEach { day ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CreamWarmTint,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Day ${day.dayNumber}: ${day.title}",
                                        fontFamily = NunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
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
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• ${day.morning}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 12.sp,
                                    color = InkNavyMuted
                                )
                                Text(
                                    text = "• ${day.afternoon}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 12.sp,
                                    color = InkNavyMuted
                                )
                                Text(
                                    text = "• ${day.evening}",
                                    fontFamily = NunitoFontFamily,
                                    fontSize = 12.sp,
                                    color = InkNavyMuted
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Packing tip: ${trip.packingTip}",
                        fontFamily = NunitoFontFamily,
                        fontSize = 12.sp,
                        color = InkNavyMuted
                    )
                }
            }
        }
    }
}
