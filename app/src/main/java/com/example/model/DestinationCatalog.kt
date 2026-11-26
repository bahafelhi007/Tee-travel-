package com.example.model

import com.example.ui.theme.AmberSun
import com.example.ui.theme.CoralRose
import com.example.ui.theme.LavenderPin
import com.example.ui.theme.SageMint

object DestinationCatalog {

    val origins = listOf(
        "From Tunisia",
        "From Paris",
        "From Casablanca",
        "From Cairo",
        "From Rome",
        "From London"
    )

    val budgetPresetsTnd = listOf(800, 1500, 2200, 3500)

    val currencies = listOf("TND", "EUR", "USD")

    val destinations: List<Destination> = listOf(
        Destination(
            id = "istanbul",
            name = "Istanbul",
            country = "Turkey",
            primaryVibe = TravelVibe.CITY,
            secondaryVibe = TravelVibe.HISTORY,
            pinColor = SageMint,
            tiltDegrees = -2.5f,
            globeXFraction = 0.48f,
            globeYFraction = 0.14f,
            estimatedBudgetTnd = 1420,
            durationDays = 4,
            bestSeason = "Apr – Jun, Sep – Nov",
            flightTimeFromTunisia = "2h 50m direct",
            tagline = "Two continents, Bosphorus ferries, and spiced tea at sunset",
            highlights = listOf(
                "Galata Tower panoramic walk",
                "Sunset ferry across the Bosphorus",
                "Grand Bazaar & Spice Market treasures",
                "Karaköy rooftop kahvaltı breakfast"
            ),
            artType = DestinationArtType.PHOTO_ISTANBUL
        ),
        Destination(
            id = "cappadocia",
            name = "Cappadocia",
            country = "Turkey",
            primaryVibe = TravelVibe.DESERT,
            secondaryVibe = TravelVibe.HISTORY,
            pinColor = CoralRose,
            tiltDegrees = 4.5f,
            globeXFraction = 0.77f,
            globeYFraction = 0.30f,
            estimatedBudgetTnd = 1680,
            durationDays = 3,
            bestSeason = "May – Oct",
            flightTimeFromTunisia = "4h 30m via IST",
            tagline = "Sunrise balloons drifting over ancient fairy-chimney valleys",
            highlights = listOf(
                "Dawn hot-air balloon valley overlook",
                "Stay in a carved Göreme cave hotel",
                "Love Valley & Red Valley sunset hike",
                "Pottery kebab dinner in Avanos"
            ),
            artType = DestinationArtType.PHOTO_CAPPADOCIA
        ),
        Destination(
            id = "santorini",
            name = "Santorini",
            country = "Greece",
            primaryVibe = TravelVibe.ISLAND,
            secondaryVibe = TravelVibe.CITY,
            pinColor = LavenderPin,
            tiltDegrees = -4.0f,
            globeXFraction = 0.18f,
            globeYFraction = 0.32f,
            estimatedBudgetTnd = 2150,
            durationDays = 4,
            bestSeason = "May – Sep",
            flightTimeFromTunisia = "3h 45m via ATH",
            tagline = "Whitewashed cliff alleys, cobalt domes, and Aegean horizons",
            highlights = listOf(
                "Oia caldera sunset walk",
                "Fira-to-Oia scenic cliff trail",
                "Catamaran cruise to Red & White beaches",
                "Fresh grilled seafood in Ammoudi Bay"
            ),
            artType = DestinationArtType.PHOTO_SANTORINI
        ),
        Destination(
            id = "tunis",
            name = "Tunis",
            country = "Tunisia",
            primaryVibe = TravelVibe.CITY,
            secondaryVibe = TravelVibe.ISLAND,
            pinColor = AmberSun,
            tiltDegrees = 2.0f,
            globeXFraction = 0.50f,
            globeYFraction = 0.45f,
            estimatedBudgetTnd = 520,
            durationDays = 3,
            bestSeason = "Mar – Jun, Sep – Nov",
            flightTimeFromTunisia = "Local weekend escape",
            tagline = "Sidi Bou Said cobalt doors, jasmine lanes & medina crafts",
            highlights = listOf(
                "Mint tea at Café des Délices in Sidi Bou Said",
                "Wandering the UNESCO Tunis Medina souks",
                "Roman baths & hillside views at Carthage",
                "Bardo Museum mosaics & La Marsa beach stroll"
            ),
            artType = DestinationArtType.CANVAS_TUNIS
        ),
        Destination(
            id = "petra",
            name = "Petra",
            country = "Jordan",
            primaryVibe = TravelVibe.HISTORY,
            secondaryVibe = TravelVibe.DESERT,
            pinColor = LavenderPin,
            tiltDegrees = 5.5f,
            globeXFraction = 0.78f,
            globeYFraction = 0.56f,
            estimatedBudgetTnd = 1890,
            durationDays = 4,
            bestSeason = "Mar – May, Oct – Nov",
            flightTimeFromTunisia = "4h 10m to Amman",
            tagline = "Rose-red stone canyons opening to the ancient Nabataean Treasury",
            highlights = listOf(
                "Walk the Siq gorge at golden morning light",
                "Petra by Night candlelit Treasury glow",
                "Climb the 800 rock steps to the Monastery",
                "Wadi Rum desert jeep & stargazing camp"
            ),
            artType = DestinationArtType.CANVAS_PETRA
        ),
        Destination(
            id = "marrakech",
            name = "Marrakech",
            country = "Morocco",
            primaryVibe = TravelVibe.DESERT,
            secondaryVibe = TravelVibe.CITY,
            pinColor = SageMint,
            tiltDegrees = -3.5f,
            globeXFraction = 0.19f,
            globeYFraction = 0.68f,
            estimatedBudgetTnd = 1290,
            durationDays = 4,
            bestSeason = "Mar – May, Sep – Nov",
            flightTimeFromTunisia = "2h 40m direct",
            tagline = "Lantern-lit riads, palm oases, and Atlas sunset silhouettes",
            highlights = listOf(
                "Jemaa el-Fnaa night food stalls & lanterns",
                "Agafay desert sunset camel ride",
                "Jardin Majorelle cobalt botanical oasis",
                "Bahia Palace tilework & traditional hammam"
            ),
            artType = DestinationArtType.CANVAS_MARRAKECH
        ),
        Destination(
            id = "dubrovnik",
            name = "Dubrovnik",
            country = "Croatia",
            primaryVibe = TravelVibe.ISLAND,
            secondaryVibe = TravelVibe.HISTORY,
            pinColor = CoralRose,
            tiltDegrees = 3.0f,
            globeXFraction = 0.53f,
            globeYFraction = 0.75f,
            estimatedBudgetTnd = 1980,
            durationDays = 4,
            bestSeason = "May – Sep",
            flightTimeFromTunisia = "3h 30m via FCO",
            tagline = "Terracotta rooftops meeting crystal Adriatic coves",
            highlights = listOf(
                "Walk the medieval Old Town stone walls",
                "Kayak around Lokrum Island sea caves",
                "Mount Srđ cable car panoramic sunset",
                "Stradun limestone promenade gelato"
            ),
            artType = DestinationArtType.CANVAS_DUBROVNIK
        )
    )

    fun findById(id: String): Destination? = destinations.find { it.id == id }
}
