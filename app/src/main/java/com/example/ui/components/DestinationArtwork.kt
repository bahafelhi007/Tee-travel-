package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DestinationArtType
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.InkNavy
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.NunitoFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageMint

@Composable
fun TeeBrandHeader(
    avatarColorIndex: Int,
    hasUnreadBadge: Boolean,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(top = 2.dp)) {
            Column(horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = stringResource(R.string.brand_tee),
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        lineHeight = 36.sp,
                        color = InkNavy,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Signature loop + coral paper airplane next to "tee"
                    Canvas(
                        modifier = Modifier
                            .size(width = 32.dp, height = 24.dp)
                            .offset(y = 2.dp)
                    ) {
                        val dashPath = Path().apply {
                            moveTo(0f, size.height * 0.85f)
                            cubicTo(
                                size.width * 0.25f, size.height * 0.55f,
                                size.width * 0.55f, size.height * 0.95f,
                                size.width * 0.65f, size.height * 0.45f
                            )
                        }
                        drawPath(
                            path = dashPath,
                            color = InkNavy.copy(alpha = 0.45f),
                            style = Stroke(
                                width = 2.2f,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                            )
                        )
                        // Small coral airplane
                        val plane = Path().apply {
                            moveTo(size.width * 0.95f, size.height * 0.12f)
                            lineTo(size.width * 0.54f, size.height * 0.30f)
                            lineTo(size.width * 0.72f, size.height * 0.42f)
                            lineTo(size.width * 0.76f, size.height * 0.65f)
                            close()
                        }
                        drawPath(path = plane, color = CoralRose)
                    }
                }
                Text(
                    text = stringResource(R.string.brand_travel),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = CoralRose,
                    modifier = Modifier.offset(x = 12.dp, y = (-6).dp)
                )
            }
        }

        // Top-right illustrated traveler avatar with yellow notification dot
        Box(
            modifier = Modifier
                .size(50.dp)
                .testTag("header_avatar_button")
                .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center
        ) {
            TravelerAvatarArtwork(
                colorIndex = avatarColorIndex,
                size = 46.dp
            )
            if (hasUnreadBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 1.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(AmberSun)
                        .border(2.dp, PureWhite, CircleShape)
                )
            }
        }
    }
}

@Composable
fun TravelerAvatarArtwork(
    colorIndex: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val avatarDesc = stringResource(R.string.cd_user_avatar)
    val bgTint = when (colorIndex % 4) {
        1 -> Color(0xFFE7EEFF)
        2 -> Color(0xFFE5F6ED)
        3 -> Color(0xFFFFF1D9)
        else -> Color(0xFFF3E9DF)
    }
    val shirtTint = when (colorIndex % 4) {
        1 -> LavenderPin
        2 -> SageMint
        3 -> AmberSun
        else -> CoralRose
    }

    Canvas(
        modifier = modifier
            .size(size)
            .shadow(4.dp, CircleShape, ambientColor = InkNavy.copy(alpha = 0.15f))
            .clip(CircleShape)
            .background(bgTint)
            .border(2.dp, PureWhite, CircleShape)
            .semantics { contentDescription = avatarDesc }
    ) {
        val w = this.size.width
        val h = this.size.height

        // Shoulders / shirt
        drawOval(
            color = shirtTint,
            topLeft = Offset(w * 0.16f, h * 0.70f),
            size = Size(w * 0.68f, h * 0.45f)
        )

        // Hair back curls
        val hairColor = Color(0xFF1F2433)
        drawCircle(color = hairColor, radius = w * 0.25f, center = Offset(w * 0.50f, h * 0.36f))
        drawCircle(color = hairColor, radius = w * 0.14f, center = Offset(w * 0.28f, h * 0.44f))
        drawCircle(color = hairColor, radius = w * 0.14f, center = Offset(w * 0.72f, h * 0.44f))
        drawCircle(color = hairColor, radius = w * 0.12f, center = Offset(w * 0.26f, h * 0.58f))
        drawCircle(color = hairColor, radius = w * 0.12f, center = Offset(w * 0.74f, h * 0.58f))

        // Neck
        drawRect(
            color = Color(0xFFEBC3A6),
            topLeft = Offset(w * 0.43f, h * 0.56f),
            size = Size(w * 0.14f, h * 0.17f)
        )

        // Face
        drawOval(
            color = Color(0xFFF3CEB3),
            topLeft = Offset(w * 0.31f, h * 0.25f),
            size = Size(w * 0.38f, h * 0.40f)
        )

        // Hair bangs
        drawOval(
            color = hairColor,
            topLeft = Offset(w * 0.32f, h * 0.18f),
            size = Size(w * 0.36f, h * 0.16f)
        )

        // Blush cheeks
        drawCircle(
            color = CoralRose.copy(alpha = 0.28f),
            radius = w * 0.048f,
            center = Offset(w * 0.38f, h * 0.49f)
        )
        drawCircle(
            color = CoralRose.copy(alpha = 0.28f),
            radius = w * 0.048f,
            center = Offset(w * 0.62f, h * 0.49f)
        )

        // Eyes
        drawCircle(color = InkNavy, radius = w * 0.025f, center = Offset(w * 0.42f, h * 0.44f))
        drawCircle(color = InkNavy, radius = w * 0.025f, center = Offset(w * 0.58f, h * 0.44f))

        // Warm smile
        drawArc(
            color = Color(0xFFB45B57),
            startAngle = 15f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(w * 0.43f, h * 0.49f),
            size = Size(w * 0.14f, h * 0.08f),
            style = Stroke(width = w * 0.024f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun DestinationArtwork(
    artType: DestinationArtType,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    when (artType) {
        DestinationArtType.PHOTO_ISTANBUL -> {
            Image(
                painter = painterResource(id = R.drawable.img_dest_istanbul_1795673484343),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        DestinationArtType.PHOTO_CAPPADOCIA -> {
            Image(
                painter = painterResource(id = R.drawable.img_dest_cappadocia_1795673484672),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        DestinationArtType.PHOTO_SANTORINI -> {
            Image(
                painter = painterResource(id = R.drawable.img_dest_santorini_1795673485648),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        DestinationArtType.CANVAS_TUNIS -> SidiBouSaidIllustration(
            contentDescription = contentDescription,
            modifier = modifier
        )
        DestinationArtType.CANVAS_PETRA -> PetraIllustration(
            contentDescription = contentDescription,
            modifier = modifier
        )
        DestinationArtType.CANVAS_MARRAKECH -> MarrakechIllustration(
            contentDescription = contentDescription,
            modifier = modifier
        )
        DestinationArtType.CANVAS_DUBROVNIK -> DubrovnikIllustration(
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

@Composable
private fun SidiBouSaidIllustration(
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription }
    ) {
        val w = size.width
        val h = size.height

        // Bright Mediterranean sky
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF7BC8F6), Color(0xFFCDEBFD))
            )
        )

        // Distant blue sea strip
        drawRect(
            color = Color(0xFF2E8BC0),
            topLeft = Offset(0f, h * 0.56f),
            size = Size(w, h * 0.16f)
        )

        // Whitewashed dome on left
        drawArc(
            color = Color(0xFFFDFDFD),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.08f, h * 0.24f),
            size = Size(w * 0.34f, h * 0.32f)
        )
        // Cobalt blue dome accent
        drawArc(
            color = Color(0xFF1F6FEB),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.55f, h * 0.16f),
            size = Size(w * 0.36f, h * 0.34f)
        )

        // Whitewashed building facade
        drawRect(
            color = Color(0xFFFCFAF7),
            topLeft = Offset(0f, h * 0.38f),
            size = Size(w, h * 0.62f)
        )

        // Iconic cobalt blue arched door
        val cobalt = Color(0xFF1A65D6)
        drawArc(
            color = cobalt,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.34f, h * 0.48f),
            size = Size(w * 0.32f, h * 0.28f)
        )
        drawRect(
            color = cobalt,
            topLeft = Offset(w * 0.34f, h * 0.62f),
            size = Size(w * 0.32f, h * 0.38f)
        )
        // Studded dots on door
        for (row in 0..2) {
            for (col in 0..2) {
                drawCircle(
                    color = Color(0xFF9AC5FF),
                    radius = w * 0.012f,
                    center = Offset(w * (0.40f + col * 0.10f), h * (0.64f + row * 0.10f))
                )
            }
        }

        // Cobblestone steps
        drawRect(
            color = Color(0xFFE7E2D8),
            topLeft = Offset(0f, h * 0.90f),
            size = Size(w, h * 0.10f)
        )

        // Magenta bougainvillea blossoms
        val blossomColors = listOf(Color(0xFFD93870), Color(0xFFEC5B8E), Color(0xFF47894B))
        drawCircle(blossomColors[2], w * 0.10f, Offset(w * 0.16f, h * 0.44f))
        drawCircle(blossomColors[0], w * 0.08f, Offset(w * 0.14f, h * 0.40f))
        drawCircle(blossomColors[1], w * 0.07f, Offset(w * 0.24f, h * 0.46f))
        drawCircle(blossomColors[0], w * 0.06f, Offset(w * 0.84f, h * 0.42f))
    }
}

@Composable
private fun PetraIllustration(
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription }
    ) {
        val w = size.width
        val h = size.height

        // Warm canyon sky
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFF6C28B), Color(0xFFE78E6B))
            )
        )

        // Rose-red sandstone cliffs
        val roseRock = Color(0xFFB85B48)
        val deepRock = Color(0xFF8C3B2E)
        val lightRock = Color(0xFFD97E62)

        drawRect(
            color = lightRock,
            topLeft = Offset(w * 0.12f, h * 0.15f),
            size = Size(w * 0.76f, h * 0.85f)
        )

        // Upper pediment & urn
        drawCircle(color = roseRock, radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.20f))
        val pediment = Path().apply {
            moveTo(w * 0.22f, h * 0.44f)
            lineTo(w * 0.50f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.44f)
            close()
        }
        drawPath(pediment, color = roseRock)

        // Treasury facade columns
        drawRect(color = deepRock, topLeft = Offset(w * 0.20f, h * 0.44f), size = Size(w * 0.60f, h * 0.07f))
        val colPositions = listOf(0.24f, 0.36f, 0.60f, 0.72f)
        colPositions.forEach { cx ->
            drawRect(
                color = Color(0xFFE5987B),
                topLeft = Offset(w * cx, h * 0.51f),
                size = Size(w * 0.045f, h * 0.43f)
            )
        }
        // Dark doorway
        drawRect(
            color = Color(0xFF3A1C19),
            topLeft = Offset(w * 0.43f, h * 0.62f),
            size = Size(w * 0.14f, h * 0.32f)
        )

        // Framing dark Siq gorge walls on left & right
        drawRect(color = deepRock, topLeft = Offset(0f, 0f), size = Size(w * 0.14f, h))
        drawRect(color = deepRock, topLeft = Offset(w * 0.86f, 0f), size = Size(w * 0.14f, h))

        // Sandy floor
        drawRect(color = Color(0xFFD8A47F), topLeft = Offset(0f, h * 0.91f), size = Size(w, h * 0.09f))
    }
}

@Composable
private fun MarrakechIllustration(
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription }
    ) {
        val w = size.width
        val h = size.height

        // Warm Sahara sunset gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF6B7AA1), Color(0xFFF39F6D), Color(0xFFFCD581))
            )
        )

        // Glowing sun
        drawCircle(
            color = Color(0xFFFFF3C4),
            radius = w * 0.14f,
            center = Offset(w * 0.56f, h * 0.44f)
        )

        // Rolling sand dunes
        val dune1 = Path().apply {
            moveTo(0f, h * 0.72f)
            quadraticTo(w * 0.35f, h * 0.58f, w * 0.75f, h * 0.72f)
            lineTo(w, h * 0.66f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(dune1, color = Color(0xFFD48148))

        val dune2 = Path().apply {
            moveTo(0f, h * 0.80f)
            quadraticTo(w * 0.55f, h * 0.68f, w, h * 0.84f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(dune2, color = Color(0xFF8B4C2B))

        // Palm tree silhouette on right
        val silhouette = Color(0xFF3E2419)
        drawLine(
            color = silhouette,
            start = Offset(w * 0.78f, h * 0.82f),
            end = Offset(w * 0.82f, h * 0.24f),
            strokeWidth = w * 0.03f,
            cap = StrokeCap.Round
        )
        val frondAngles = listOf(-150f, -110f, -60f, -20f, 20f)
        frondAngles.forEach { angle ->
            val rad = Math.toRadians(angle.toDouble())
            val endX = w * 0.82f + (w * 0.18f * kotlin.math.cos(rad)).toFloat()
            val endY = h * 0.24f + (h * 0.14f * kotlin.math.sin(rad)).toFloat()
            drawLine(
                color = silhouette,
                start = Offset(w * 0.82f, h * 0.24f),
                end = Offset(endX, endY),
                strokeWidth = w * 0.022f,
                cap = StrokeCap.Round
            )
        }

        // Small camel silhouette on dune ridge
        drawRoundRect(
            color = silhouette,
            topLeft = Offset(w * 0.24f, h * 0.63f),
            size = Size(w * 0.10f, h * 0.055f),
            cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
        )
        drawLine(
            color = silhouette,
            start = Offset(w * 0.26f, h * 0.68f),
            end = Offset(w * 0.25f, h * 0.74f),
            strokeWidth = w * 0.015f
        )
        drawLine(
            color = silhouette,
            start = Offset(w * 0.32f, h * 0.68f),
            end = Offset(w * 0.33f, h * 0.74f),
            strokeWidth = w * 0.015f
        )
        drawLine(
            color = silhouette,
            start = Offset(w * 0.33f, h * 0.64f),
            end = Offset(w * 0.36f, h * 0.59f),
            strokeWidth = w * 0.016f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun DubrovnikIllustration(
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .semantics { this.contentDescription = contentDescription }
    ) {
        val w = size.width
        val h = size.height

        // Bright Adriatic coastal sky
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF72BCEE), Color(0xFFD4EEFF))
            )
        )

        // Deep turquoise Adriatic sea
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1D7FB8), Color(0xFF2CA6C9))
            ),
            topLeft = Offset(0f, h * 0.42f),
            size = Size(w, h * 0.58f)
        )

        // Terracotta Old Town cluster
        val wallColor = Color(0xFFEBE2D3)
        val roofColor = Color(0xFFD65D3B)
        val roofDark = Color(0xFFB54428)

        drawRect(color = wallColor, topLeft = Offset(w * 0.08f, h * 0.52f), size = Size(w * 0.54f, h * 0.48f))

        val roofs = listOf(
            Triple(0.06f, 0.44f, 0.22f),
            Triple(0.24f, 0.38f, 0.20f),
            Triple(0.40f, 0.46f, 0.20f),
            Triple(0.14f, 0.60f, 0.22f),
            Triple(0.34f, 0.62f, 0.24f)
        )
        roofs.forEachIndexed { idx, (rx, ry, rw) ->
            val path = Path().apply {
                moveTo(w * rx, h * (ry + 0.10f))
                lineTo(w * (rx + rw * 0.5f), h * ry)
                lineTo(w * (rx + rw), h * (ry + 0.10f))
                close()
            }
            drawPath(path, color = if (idx % 2 == 0) roofColor else roofDark)
        }

        // Tiny white sailboat in the harbor
        val sail = Path().apply {
            moveTo(w * 0.78f, h * 0.62f)
            lineTo(w * 0.78f, h * 0.48f)
            lineTo(w * 0.87f, h * 0.62f)
            close()
        }
        drawPath(sail, color = PureWhite)
        drawRect(color = Color(0xFF5A3D31), topLeft = Offset(w * 0.74f, h * 0.63f), size = Size(w * 0.14f, h * 0.03f))
    }
}
