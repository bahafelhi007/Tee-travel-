package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Destination
import com.example.model.TravelVibe
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CompassInk
import com.example.ui.theme.CoralRose
import com.example.ui.theme.InkNavy
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PatrickHandFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageMint
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun InteractiveTravelGlobeStage(
    destinations: List<Destination>,
    savedIds: Set<String>,
    selectedVibe: TravelVibe,
    budgetTnd: Int,
    onDestinationClick: (Destination) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Interactive drag parallax with smooth spring return
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    val animatedPanX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "globePanX"
    )
    val animatedPanY by animateFloatAsState(
        targetValue = dragOffsetY,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "globePanY"
    )

    // Gentle living animations for dashed routes & floating feel
    val infiniteTransition = rememberInfiniteTransition(label = "globeAmbient")
    val dashPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dashPhase"
    )
    val floatY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    val globeContentDesc = stringResource(R.string.cd_world_globe)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                    },
                    onDragCancel = {
                        dragOffsetX = 0f
                        dragOffsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffsetX = (dragOffsetX + dragAmount.x * 0.45f).coerceIn(-38f, 38f)
                        dragOffsetY = (dragOffsetY + dragAmount.y * 0.45f).coerceIn(-28f, 28f)
                    }
                )
            }
    ) {
        val stageW = maxWidth
        val stageH = maxHeight
        val globeSize = minOf(stageW * 1.06f, stageH * 0.96f)
        val density = LocalDensity.current
        val stageWPx = with(density) { stageW.toPx() }
        val stageHPx = with(density) { stageH.toPx() }

        // Watercolor globe image centered slightly low so top annotations have breathing room
        Box(
            modifier = Modifier
                .size(globeSize)
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        x = (animatedPanX * 0.55f).roundToInt(),
                        y = (animatedPanY * 0.55f + 14.dp.toPx()).roundToInt()
                    )
                }
                .clip(CircleShape)
                .graphicsLayer {
                    rotationZ = animatedPanX * 0.08f
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_world_globe_1795673483736),
                contentDescription = globeContentDesc,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.13f)
            )
        }

        // Hand-drawn route paths & travel doodles layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val px = animatedPanX * 0.7f
            val py = animatedPanY * 0.7f

            // Origin pin coordinates (near Tunisia / central Mediterranean)
            val originX = w * 0.43f + px
            val originY = h * 0.44f + py

            // Animated dashed travel arcs from origin to key destinations
            val routeTargets = listOf(
                Offset(w * 0.50f + px, h * 0.24f + py), // Istanbul
                Offset(w * 0.74f + px, h * 0.35f + py), // Cappadocia
                Offset(w * 0.24f + px, h * 0.38f + py), // Santorini
                Offset(w * 0.25f + px, h * 0.66f + py), // Marrakech
                Offset(w * 0.53f + px, h * 0.74f + py)  // Dubrovnik
            )
            routeTargets.forEachIndexed { idx, target ->
                val midX = (originX + target.x) / 2f + (if (idx % 2 == 0) -24f else 24f)
                val midY = (originY + target.y) / 2f - 18f
                val path = Path().apply {
                    moveTo(originX, originY)
                    quadraticTo(midX, midY, target.x, target.y)
                }
                drawPath(
                    path = path,
                    color = InkNavy.copy(alpha = 0.38f),
                    style = Stroke(
                        width = 2.6f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(9f, 9f), dashPhase)
                    )
                )
            }

            // Origin red map pin
            drawCircle(
                color = CoralRose,
                radius = 9f,
                center = Offset(originX, originY - 8f)
            )
            drawCircle(
                color = PureWhite,
                radius = 3.5f,
                center = Offset(originX, originY - 8f)
            )

            // Dashed curved arrow under "Let's explore" (top-left)
            val exploreArrow = Path().apply {
                moveTo(w * 0.08f, h * 0.13f)
                cubicTo(w * 0.04f, h * 0.20f, w * 0.07f, h * 0.23f, w * 0.13f, h * 0.22f)
            }
            drawPath(
                path = exploreArrow,
                color = InkNavy.copy(alpha = 0.65f),
                style = Stroke(
                    width = 2.5f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 7f), 0f)
                )
            )
            // Small arrowhead
            drawLine(
                color = InkNavy.copy(alpha = 0.65f),
                start = Offset(w * 0.115f, h * 0.205f),
                end = Offset(w * 0.135f, h * 0.22f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = InkNavy.copy(alpha = 0.65f),
                start = Offset(w * 0.115f, h * 0.235f),
                end = Offset(w * 0.135f, h * 0.22f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Top-right hot air balloon doodle
            val balloonCenter = Offset(w * 0.87f, h * 0.12f + floatY)
            drawOval(
                color = InkNavy.copy(alpha = 0.65f),
                topLeft = Offset(balloonCenter.x - 16f, balloonCenter.y - 20f),
                size = Size(32f, 36f),
                style = Stroke(width = 2.4f)
            )
            drawLine(
                color = InkNavy.copy(alpha = 0.55f),
                start = Offset(balloonCenter.x, balloonCenter.y - 20f),
                end = Offset(balloonCenter.x, balloonCenter.y + 16f),
                strokeWidth = 1.8f
            )
            drawRect(
                color = InkNavy.copy(alpha = 0.65f),
                topLeft = Offset(balloonCenter.x - 6f, balloonCenter.y + 20f),
                size = Size(12f, 9f),
                style = Stroke(width = 2f)
            )

            // Mid-left sailboat doodle
            val boatX = w * 0.06f
            val boatY = h * 0.54f - floatY * 0.6f
            val boatSail = Path().apply {
                moveTo(boatX, boatY)
                lineTo(boatX + 12f, boatY - 24f)
                lineTo(boatX + 24f, boatY)
                close()
            }
            drawPath(boatSail, color = SageMint.copy(alpha = 0.75f))
            drawPath(
                boatSail,
                color = InkNavy.copy(alpha = 0.7f),
                style = Stroke(width = 2f)
            )
            drawLine(
                color = InkNavy.copy(alpha = 0.8f),
                start = Offset(boatX - 4f, boatY + 4f),
                end = Offset(boatX + 28f, boatY + 4f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )

            // Bottom-left little flying coral airplane + dashed trail
            val planeTrail = Path().apply {
                moveTo(w * 0.14f + px, h * 0.83f + py)
                quadraticTo(w * 0.22f + px, h * 0.89f + py, w * 0.30f + px, h * 0.88f + py)
            }
            drawPath(
                path = planeTrail,
                color = InkNavy.copy(alpha = 0.5f),
                style = Stroke(
                    width = 2.4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), dashPhase)
                )
            )
            val planeTip = Offset(w * 0.34f + px, h * 0.87f + py)
            val littlePlane = Path().apply {
                moveTo(planeTip.x, planeTip.y)
                lineTo(planeTip.x - 24f, planeTip.y - 6f)
                lineTo(planeTip.x - 16f, planeTip.y + 2f)
                lineTo(planeTip.x - 22f, planeTip.y + 10f)
                close()
            }
            drawPath(littlePlane, color = CoralRose)
            drawPath(littlePlane, color = InkNavy, style = Stroke(width = 1.6f))
        }

        // Handwritten map labels & doodles
        Text(
            text = "${stringResource(R.string.label_lets_explore)} ♥",
            fontFamily = PatrickHandFontFamily,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            color = InkNavy,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 18.dp, top = 6.dp)
                .rotate(-6f)
        )

        Text(
            text = stringResource(R.string.vibe_city),
            fontFamily = PatrickHandFontFamily,
            fontSize = 15.sp,
            color = InkNavy.copy(alpha = 0.85f),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (stageWPx * 0.60f + animatedPanX * 0.5f).roundToInt(),
                        y = (stageHPx * 0.18f + animatedPanY * 0.5f).roundToInt()
                    )
                }
        )

        Text(
            text = stringResource(R.string.vibe_island),
            fontFamily = PatrickHandFontFamily,
            fontSize = 15.sp,
            lineHeight = 16.sp,
            color = InkNavy.copy(alpha = 0.85f),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (stageWPx * 0.04f).roundToInt(),
                        y = (stageHPx * 0.33f).roundToInt()
                    )
                }
                .rotate(-4f)
        )

        Text(
            text = stringResource(R.string.vibe_desert),
            fontFamily = PatrickHandFontFamily,
            fontSize = 15.sp,
            lineHeight = 16.sp,
            color = InkNavy.copy(alpha = 0.85f),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (stageWPx * 0.04f).roundToInt(),
                        y = (stageHPx * 0.68f).roundToInt()
                    )
                }
                .rotate(-3f)
        )

        Text(
            text = stringResource(R.string.vibe_history),
            fontFamily = PatrickHandFontFamily,
            fontSize = 15.sp,
            lineHeight = 16.sp,
            color = InkNavy.copy(alpha = 0.85f),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (stageWPx * 0.80f).roundToInt(),
                        y = (stageHPx * 0.64f).roundToInt()
                    )
                }
                .rotate(5f)
        )

        // Vintage "ADVENTURE AWAITS" Compass Stamp bottom-right
        AdventureCompassBadge(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 10.dp)
                .rotate(-8f)
        )

        // Polaroid Destination Cards laid out around the globe
        val cardWidth = if (stageW < 380.dp) 88.dp else 98.dp
        val cardWidthPx = with(density) { cardWidth.toPx() }
        val cardHeightPx = with(density) { (cardWidth * 1.15f).toPx() }

        destinations.forEachIndexed { index, dest ->
            val matchesVibe = dest.matchesVibe(selectedVibe)
            val withinBudget = dest.estimatedBudgetTnd <= budgetTnd + 250
            val isHighlighted = matchesVibe && withinBudget
            val isSaved = savedIds.contains(dest.id)

            val centerX = stageWPx * dest.globeXFraction + animatedPanX
            val centerY = stageHPx * dest.globeYFraction + animatedPanY + (if (index % 2 == 0) floatY else -floatY)

            PolaroidDestinationCard(
                destination = dest,
                isSaved = isSaved,
                isDimmed = selectedVibe != TravelVibe.ALL && !matchesVibe,
                isHighlighted = selectedVibe != TravelVibe.ALL && isHighlighted,
                cardWidth = cardWidth,
                onClick = { onDestinationClick(dest) },
                onToggleFavorite = { onToggleFavorite(dest.id) },
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (centerX - cardWidthPx / 2f).roundToInt().coerceIn(8, (stageWPx - cardWidthPx - 8).roundToInt().coerceAtLeast(8)),
                            y = (centerY - cardHeightPx / 2f).roundToInt().coerceIn(8, (stageHPx - cardHeightPx - 8).roundToInt().coerceAtLeast(8))
                        )
                    }
            )
        }
    }
}

@Composable
fun PolaroidDestinationCard(
    destination: Destination,
    isSaved: Boolean,
    isDimmed: Boolean,
    isHighlighted: Boolean,
    cardWidth: Dp,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = when {
            isHighlighted -> 1.07f
            isDimmed -> 0.90f
            else -> 1.0f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "polaroidScale"
    )
    val heartScale by animateFloatAsState(
        targetValue = if (isSaved) 1.22f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "heartPop"
    )
    val cardAlpha by animateFloatAsState(
        targetValue = if (isDimmed) 0.55f else 1.0f,
        label = "polaroidAlpha"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .alpha(cardAlpha)
            .rotate(destination.tiltDegrees)
    ) {
        Column(
            modifier = Modifier
                .width(cardWidth)
                .shadow(
                    elevation = if (isHighlighted) 12.dp else 7.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = InkNavy.copy(alpha = 0.22f)
                )
                .clip(RoundedCornerShape(10.dp))
                .background(PureWhite)
                .then(
                    if (isHighlighted) {
                        Modifier.border(1.8.dp, destination.pinColor, RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                    }
                )
                .testTag("polaroid_card_${destination.id}")
                .clickable(onClick = onClick)
                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 4.dp)
        ) {
            DestinationArtwork(
                artType = destination.artType,
                contentDescription = "${destination.name}, ${destination.country}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardWidth * 0.72f)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = destination.name,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = InkNavy,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .testTag("heart_button_${destination.id}")
                        .clickable(onClick = onToggleFavorite),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorite_toggle),
                        tint = if (isSaved) CoralRose else destination.pinColor,
                        modifier = Modifier
                            .size(14.dp)
                            .scale(heartScale)
                    )
                }
            }
        }

        // Colored pushpin dot on top-left corner of the Polaroid
        Box(
            modifier = Modifier
                .offset(x = (-3).dp, y = (-4).dp)
                .size(12.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(destination.pinColor)
                .border(1.5.dp, PureWhite, CircleShape)
        )
    }
}

@Composable
private fun AdventureCompassBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(84.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2f, size.height / 2f)
            val outerR = size.minDimension * 0.45f
            val innerR = size.minDimension * 0.28f

            drawCircle(
                color = CompassInk.copy(alpha = 0.25f),
                radius = outerR,
                center = c,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = CompassInk.copy(alpha = 0.20f),
                radius = innerR,
                center = c,
                style = Stroke(width = 1.5f)
            )

            // 8-point compass star
            for (i in 0 until 8) {
                val angle = Math.toRadians((i * 45).toDouble())
                val len = if (i % 2 == 0) innerR * 0.95f else innerR * 0.55f
                val tip = Offset(
                    c.x + (len * cos(angle)).toFloat(),
                    c.y + (len * sin(angle)).toFloat()
                )
                drawLine(
                    color = CompassInk.copy(alpha = 0.45f),
                    start = c,
                    end = tip,
                    strokeWidth = if (i % 2 == 0) 2.2f else 1.5f,
                    cap = StrokeCap.Round
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.compass_adventure),
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                color = CompassInk.copy(alpha = 0.72f),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.compass_awaits),
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                color = CompassInk.copy(alpha = 0.72f),
                letterSpacing = 0.8.sp
            )
        }
    }
}
