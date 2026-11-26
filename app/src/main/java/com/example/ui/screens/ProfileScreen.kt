package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DestinationCatalog
import com.example.model.TravelVibe
import com.example.model.TravelerProfile
import com.example.model.formatBudget
import com.example.ui.components.TravelerAvatarArtwork
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.CreamWarmTint
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.InkNavy
import com.example.ui.theme.InkNavyMuted
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageMint
import com.example.ui.theme.SoftBorder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    profile: TravelerProfile,
    savedCount: Int,
    tripsCount: Int,
    onUpdateName: (String) -> Unit,
    onSelectAvatarColor: (Int) -> Unit,
    onSelectOrigin: (String) -> Unit,
    onSelectCurrency: (String) -> Unit,
    onSelectVibe: (TravelVibe) -> Unit,
    onBackToPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToPlan)

    var editingName by remember { mutableStateOf(false) }
    var draftName by remember(profile.name) { mutableStateOf(profile.name) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Traveler Profile",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Profile Identity Card
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = PureWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = InkNavy.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TravelerAvatarArtwork(
                        colorIndex = profile.avatarColorIndex,
                        size = 86.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Avatar palette picker
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        val swatches = listOf(CoralRose, LavenderPin, SageMint, AmberSun)
                        swatches.forEachIndexed { idx, color ->
                            val active = profile.avatarColorIndex == idx
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (active) 3.dp else 1.dp,
                                        color = if (active) InkNavy else PureWhite,
                                        shape = CircleShape
                                    )
                                    .clickable { onSelectAvatarColor(idx) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (editingName) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = draftName,
                                onValueChange = { draftName = it },
                                singleLine = true,
                                label = { Text("Your name") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    onUpdateName(draftName)
                                    editingName = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = "Save name", tint = PureWhite)
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { editingName = true }
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = profile.name,
                                fontFamily = FredokaFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = InkNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit name",
                                tint = InkNavyMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "${profile.homeOrigin} • ${formatBudget(profile.defaultBudgetTnd, profile.currencyCode)} budget",
                        fontFamily = NunitoFontFamily,
                        fontSize = 13.sp,
                        color = InkNavyMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox(
                            count = savedCount.toString(),
                            label = "Saved Spots",
                            accent = CoralRose,
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            count = tripsCount.toString(),
                            label = "Planned Trips",
                            accent = LavenderPin,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Currency Selector
            Text(
                text = "Display currency",
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DestinationCatalog.currencies.forEach { code ->
                    val selected = code == profile.currencyCode
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) InkNavy else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, if (selected) InkNavy else SoftBorder, RoundedCornerShape(18.dp))
                            .testTag("currency_chip_$code")
                            .clickable { onSelectCurrency(code) }
                    ) {
                        Text(
                            text = code,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (selected) PureWhite else InkNavy,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Default Origin Selector
            Text(
                text = "Home departure",
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DestinationCatalog.origins.forEach { origin ->
                    val selected = origin == profile.homeOrigin
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) LavenderPin else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, if (selected) LavenderPin else SoftBorder, RoundedCornerShape(18.dp))
                            .clickable { onSelectOrigin(origin) }
                    ) {
                        Text(
                            text = origin,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selected) PureWhite else InkNavy,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Favorite Vibe Selector
            Text(
                text = "Default travel vibe",
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = InkNavy
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TravelVibe.entries.forEach { vibe ->
                    val selected = vibe == profile.preferredVibe
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) vibe.accentColor.copy(alpha = 0.22f) else PureWhite,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .border(
                                1.5.dp,
                                if (selected) vibe.accentColor else SoftBorder,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { onSelectVibe(vibe) }
                    ) {
                        Text(
                            text = vibe.shortTitle,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = InkNavy,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StatBox(
    count: String,
    label: String,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CreamWarmTint,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = accent
            )
            Text(
                text = label,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = InkNavyMuted
            )
        }
    }
}
