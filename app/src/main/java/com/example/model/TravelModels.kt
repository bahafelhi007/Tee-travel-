package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.SageMint

enum class AppTab(val label: String) {
    PLAN("Plan"),
    SAVED("Saved"),
    TRIPS("Trips"),
    PROFILE("Profile")
}

enum class TravelVibe(
    val label: String,
    val shortTitle: String,
    val subtitle: String,
    val accentColor: Color
) {
    ALL("Vibe", "All Vibes", "Every kind of adventure", AmberSun),
    CITY("City vibes", "City vibes", "Bazaars, rooftops & street cafés", SageMint),
    ISLAND("Island escape", "Island escape", "Blue domes, coves & sea breeze", LavenderPin),
    DESERT("Desert mood", "Desert mood", "Golden dunes, palms & starry skies", AmberSun),
    HISTORY("History calling", "History calling", "Ancient ruins & timeless stone", CoralRose)
}

enum class DestinationArtType {
    PHOTO_ISTANBUL,
    PHOTO_CAPPADOCIA,
    PHOTO_SANTORINI,
    CANVAS_TUNIS,
    CANVAS_PETRA,
    CANVAS_MARRAKECH,
    CANVAS_DUBROVNIK
}

data class Destination(
    val id: String,
    val name: String,
    val country: String,
    val primaryVibe: TravelVibe,
    val secondaryVibe: TravelVibe?,
    val pinColor: Color,
    val tiltDegrees: Float,
    val globeXFraction: Float,
    val globeYFraction: Float,
    val estimatedBudgetTnd: Int,
    val durationDays: Int,
    val bestSeason: String,
    val flightTimeFromTunisia: String,
    val tagline: String,
    val highlights: List<String>,
    val artType: DestinationArtType
) {
    fun matchesVibe(vibe: TravelVibe): Boolean =
        vibe == TravelVibe.ALL || primaryVibe == vibe || secondaryVibe == vibe
}

data class ItineraryDay(
    val dayNumber: Int,
    val title: String,
    val morning: String,
    val afternoon: String,
    val evening: String,
    val estimatedCostTnd: Int
)

data class GeneratedTravelPlan(
    val id: String,
    val destinationId: String,
    val destinationName: String,
    val country: String,
    val origin: String,
    val budgetTnd: Int,
    val vibeLabel: String,
    val summary: String,
    val flightEstimateTnd: Int,
    val stayEstimateTnd: Int,
    val foodAndActivitiesTnd: Int,
    val packingTip: String,
    val localTip: String,
    val days: List<ItineraryDay>,
    val isSamplePreview: Boolean = true
)

data class TravelerProfile(
    val name: String = "Lina Ben Ali",
    val homeOrigin: String = "From Tunisia",
    val defaultBudgetTnd: Int = 1500,
    val currencyCode: String = "TND",
    val preferredVibe: TravelVibe = TravelVibe.ALL,
    val avatarColorIndex: Int = 0
)

fun formatBudget(amountTnd: Int, currencyCode: String): String {
    val converted = when (currencyCode) {
        "EUR" -> (amountTnd * 0.30).toInt()
        "USD" -> (amountTnd * 0.32).toInt()
        else -> amountTnd
    }
    val formatted = "%,d".format(converted)
    return "$formatted $currencyCode"
}
