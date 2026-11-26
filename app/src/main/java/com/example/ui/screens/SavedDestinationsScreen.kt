package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.model.Destination
import com.example.model.DestinationCatalog
import com.example.model.TravelVibe
import com.example.model.formatBudget
import com.example.ui.components.DestinationArtwork
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CoralSoft
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.InkNavy
import com.example.ui.theme.InkNavyMuted
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SoftBorder

@Composable
fun SavedDestinationsScreen(
    savedIds: Set<String>,
    currencyCode: String,
    onDestinationClick: (Destination) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onExploreMapClick: () -> Unit,
    onBackToPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToPlan)
    var activeFilter by remember { mutableStateOf(TravelVibe.ALL) }

    val savedDestinations = remember(savedIds, activeFilter) {
        DestinationCatalog.destinations
            .filter { savedIds.contains(it.id) && it.matchesVibe(activeFilter) }
    }

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
            Text(
                text = "Saved Scrapbook",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = InkNavy
            )
            Text(
                text = if (savedIds.isEmpty()) "Collect destinations you dream of visiting"
                else "${savedIds.size} pinned spot${if (savedIds.size == 1) "" else "s"} ready for your next getaway",
                fontFamily = NunitoFontFamily,
                fontSize = 14.sp,
                color = InkNavyMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Vibe filter chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TravelVibe.entries) { vibe ->
                    val selected = vibe == activeFilter
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) InkNavy else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (selected) InkNavy else SoftBorder, RoundedCornerShape(20.dp))
                            .clickable { activeFilter = vibe }
                    ) {
                        Text(
                            text = vibe.shortTitle,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selected) PureWhite else InkNavy,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (savedDestinations.isEmpty()) {
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
                                .background(CoralSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = CoralRose,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.saved_empty_title),
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = InkNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.saved_empty_subtitle),
                            fontFamily = NunitoFontFamily,
                            fontSize = 14.sp,
                            color = InkNavyMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onExploreMapClick,
                            colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Explore,
                                contentDescription = null,
                                tint = PureWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Explore the globe",
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 155.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(savedDestinations, key = { it.id }) { dest ->
                        SavedPolaroidGridCard(
                            destination = dest,
                            currencyCode = currencyCode,
                            onClick = { onDestinationClick(dest) },
                            onRemoveFavorite = { onToggleFavorite(dest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedPolaroidGridCard(
    destination: Destination,
    currencyCode: String,
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = InkNavy.copy(alpha = 0.14f))
            .clip(RoundedCornerShape(16.dp))
            .testTag("saved_card_${destination.id}")
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                DestinationArtwork(
                    artType = destination.artType,
                    contentDescription = "${destination.name}, ${destination.country}",
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onRemoveFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(PureWhite.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(R.string.cd_favorite_toggle),
                        tint = CoralRose,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = destination.name,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = InkNavy
                    )
                    Text(
                        text = destination.primaryVibe.shortTitle,
                        fontFamily = NunitoFontFamily,
                        fontSize = 12.sp,
                        color = InkNavyMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CoralSoft
                ) {
                    Text(
                        text = formatBudget(destination.estimatedBudgetTnd, currencyCode),
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = CoralRose,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
