package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SentimentSatisfiedAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppTab
import com.example.model.TravelVibe
import com.example.model.formatBudget
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.InkNavy
import com.example.ui.theme.InkNavyMuted
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite

@Composable
fun FilterPillsRow(
    budgetTnd: Int,
    currencyCode: String,
    origin: String,
    selectedVibe: TravelVibe,
    onBudgetClick: () -> Unit,
    onOriginClick: () -> Unit,
    onVibeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TravelFilterPill(
            icon = Icons.Outlined.AccountBalanceWallet,
            iconTint = CoralRose,
            text = formatBudget(budgetTnd, currencyCode),
            testTag = "filter_budget_pill",
            onClick = onBudgetClick,
            modifier = Modifier.weight(1.05f)
        )
        TravelFilterPill(
            icon = Icons.Outlined.LocationOn,
            iconTint = LavenderPin,
            text = origin,
            testTag = "filter_origin_pill",
            onClick = onOriginClick,
            modifier = Modifier.weight(1.15f)
        )
        TravelFilterPill(
            icon = Icons.Outlined.SentimentSatisfiedAlt,
            iconTint = if (selectedVibe == TravelVibe.ALL) AmberSun else selectedVibe.accentColor,
            text = selectedVibe.label,
            testTag = "filter_vibe_pill",
            onClick = onVibeClick,
            modifier = Modifier.weight(0.95f)
        )
    }
}

@Composable
private fun TravelFilterPill(
    icon: ImageVector,
    iconTint: Color,
    text: String,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pillScale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .height(44.dp)
            .minimumInteractiveComponentSize()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = InkNavy.copy(alpha = 0.14f)
            )
            .clip(RoundedCornerShape(24.dp))
            .testTag(testTag)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        color = PureWhite
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = InkNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = InkNavyMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun FloatingGetPlansPillRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "topCtaScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Curly hand-drawn ink arrow pointing from the budget pill toward "Get my plans"
        Canvas(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 34.dp)
                .size(width = 58.dp, height = 44.dp)
        ) {
            val w = size.width
            val h = size.height
            val loopPath = Path().apply {
                moveTo(w * 0.08f, h * 0.05f)
                cubicTo(w * 0.02f, h * 0.55f, w * 0.38f, h * 0.72f, w * 0.42f, h * 0.50f)
                cubicTo(w * 0.44f, h * 0.34f, w * 0.24f, h * 0.38f, w * 0.30f, h * 0.62f)
                cubicTo(w * 0.36f, h * 0.85f, w * 0.74f, h * 0.82f, w * 0.94f, h * 0.74f)
            }
            drawPath(
                path = loopPath,
                color = InkNavy,
                style = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            // Arrowhead
            drawLine(
                color = InkNavy,
                start = OffsetStart(w * 0.80f, h * 0.62f),
                end = OffsetStart(w * 0.95f, h * 0.74f),
                strokeWidth = 3.2f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = InkNavy,
                start = OffsetStart(w * 0.83f, h * 0.88f),
                end = OffsetStart(w * 0.95f, h * 0.74f),
                strokeWidth = 3.2f,
                cap = StrokeCap.Round
            )
        }

        Surface(
            modifier = Modifier
                .scale(scale)
                .rotate(-2.2f)
                .shadow(10.dp, RoundedCornerShape(28.dp), ambientColor = InkNavy.copy(alpha = 0.28f))
                .clip(RoundedCornerShape(28.dp))
                .minimumInteractiveComponentSize()
                .testTag("top_get_plans_button")
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(28.dp),
            color = InkNavy
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cta_get_my_plans),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = AmberSun,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private fun OffsetStart(x: Float, y: Float) = androidx.compose.ui.geometry.Offset(x, y)

@Composable
fun BottomGetPlansButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bottomCtaScale"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "sparklePulse")
    val sparkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .scale(scale)
            .height(56.dp)
            .shadow(12.dp, RoundedCornerShape(28.dp), ambientColor = InkNavy.copy(alpha = 0.30f))
            .clip(RoundedCornerShape(28.dp))
            .testTag("bottom_get_plans_button")
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(28.dp),
        color = InkNavy
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(PureWhite.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFBAC4DB).copy(alpha = sparkleAlpha),
                    modifier = Modifier.size(18.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.cta_get_my_plans),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = AmberSun,
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.cd_generate_plans),
                    tint = CoralRose,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun TeeBottomNavigationBar(
    selectedTab: AppTab,
    savedCount: Int,
    tripsCount: Int,
    onSelectTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = PureWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val icon = when (tab) {
                    AppTab.PLAN -> if (isSelected) Icons.Filled.LocationOn else Icons.Outlined.LocationOn
                    AppTab.SAVED -> if (isSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                    AppTab.TRIPS -> if (isSelected) Icons.Filled.Luggage else Icons.Outlined.Luggage
                    AppTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
                }
                val badgeCount = when (tab) {
                    AppTab.SAVED -> savedCount
                    AppTab.TRIPS -> tripsCount
                    else -> 0
                }
                val activeColor = CoralRose

                Column(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .testTag("nav_tab_${tab.name.lowercase()}")
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) activeColor else InkNavyMuted,
                            modifier = Modifier.size(24.dp)
                        )
                        if (badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-3).dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(CoralRose),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeCount.coerceAtMost(9).toString(),
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = PureWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.label,
                        fontFamily = NunitoFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (isSelected) activeColor else InkNavyMuted
                    )

                    // Signature coral wavy squiggle underline under active tab
                    Box(
                        modifier = Modifier
                            .height(7.dp)
                            .width(34.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = isSelected,
                            enter = fadeIn() + scaleIn()
                        ) {
                            Canvas(modifier = Modifier.size(width = 34.dp, height = 6.dp)) {
                                val w = size.width
                                val h = size.height
                                val squiggle = Path().apply {
                                    moveTo(0f, h * 0.6f)
                                    quadraticTo(w * 0.125f, 0f, w * 0.25f, h * 0.6f)
                                    quadraticTo(w * 0.375f, h * 1.1f, w * 0.50f, h * 0.5f)
                                    quadraticTo(w * 0.625f, 0f, w * 0.75f, h * 0.6f)
                                    quadraticTo(w * 0.875f, h * 1.1f, w, h * 0.5f)
                                }
                                drawPath(
                                    path = squiggle,
                                    color = CoralRose,
                                    style = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
