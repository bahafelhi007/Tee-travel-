package com.example.data.ai

import com.example.model.Destination
import com.example.model.DestinationCatalog
import com.example.model.GeneratedTravelPlan
import com.example.model.ItineraryDay
import com.example.model.TravelVibe
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.math.abs

sealed interface AiPlanResult {
    data class Success(
        val plans: List<GeneratedTravelPlan>,
        val isSamplePreview: Boolean
    ) : AiPlanResult

    data class Error(val message: String) : AiPlanResult
}

interface TravelPlanAiRepository {
    suspend fun generatePlans(
        origin: String,
        budgetTnd: Int,
        vibe: TravelVibe,
        focusDestination: Destination? = null
    ): AiPlanResult
}

/**
 * Sample placeholder implementation used when Firebase AI Logic is not enabled.
 * Generates structured sample itineraries tailored to the user's selected budget, origin, and vibe.
 */
class SampleTravelPlanAiRepository : TravelPlanAiRepository {

    override suspend fun generatePlans(
        origin: String,
        budgetTnd: Int,
        vibe: TravelVibe,
        focusDestination: Destination?
    ): AiPlanResult {
        delay(650L)

        val candidates = if (focusDestination != null) {
            listOf(focusDestination)
        } else {
            val vibeMatches = DestinationCatalog.destinations.filter { it.matchesVibe(vibe) }
            val pool = vibeMatches.ifEmpty { DestinationCatalog.destinations }
            pool.sortedBy { abs(it.estimatedBudgetTnd - budgetTnd) }.take(3)
        }

        val plans = candidates.map { dest ->
            buildSamplePlan(
                destination = dest,
                origin = origin,
                targetBudgetTnd = budgetTnd,
                vibe = vibe
            )
        }

        return AiPlanResult.Success(plans = plans, isSamplePreview = true)
    }

    private fun buildSamplePlan(
        destination: Destination,
        origin: String,
        targetBudgetTnd: Int,
        vibe: TravelVibe
    ): GeneratedTravelPlan {
        val effectiveBudget = targetBudgetTnd.coerceAtLeast(400)
        val isLocal = destination.id == "tunis" && origin.contains("Tunisia", ignoreCase = true)
        val flightPct = if (isLocal) 0.12f else 0.42f
        val stayPct = if (isLocal) 0.45f else 0.33f
        val flightCost = (effectiveBudget * flightPct).toInt()
        val stayCost = (effectiveBudget * stayPct).toInt()
        val foodCost = (effectiveBudget - flightCost - stayCost).coerceAtLeast(100)

        val activeVibeLabel = if (vibe == TravelVibe.ALL) destination.primaryVibe.label else vibe.label
        val dayCount = destination.durationDays.coerceIn(3, 4)
        val perDayBudget = (foodCost / dayCount).coerceAtLeast(60)

        val days = when (destination.id) {
            "istanbul" -> listOf(
                ItineraryDay(
                    1,
                    "Galata & Karaköy Arrival",
                    "Arrive $origin, drop bags in Karaköy and enjoy Turkish simit & çay.",
                    "Climb Galata Tower for sweeping Golden Horn views and wander cobbled boutiques.",
                    "Sunset Bosphorus ferry ride followed by grilled sea bass by the bridge.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Old City Heritage & Bazaars",
                    "Early entry to Hagia Sophia & Blue Mosque courtyard before peak crowds.",
                    "Spice Bazaar tastings and lantern-lit alleys of the Grand Bazaar.",
                    "Rooftop meze dinner overlooking lit minarets.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Asian Side & Sunset Tea",
                    "Morning ferry to Kadıköy for Moda coastal walk and specialty coffee.",
                    "Explore colorful Yeldeğirmeni street art and local antique shops.",
                    "Sunset tea cushions along Üsküdar promenade facing Maiden's Tower.",
                    perDayBudget
                ),
                ItineraryDay(
                    4,
                    "Ortaköy & Farewell",
                    "Waterside kumpir and Ortaköy Mosque photos by the Bosphorus.",
                    "Pick up Turkish delight and hand-painted ceramics before heading to the airport.",
                    "Return flight $origin.",
                    perDayBudget / 2
                )
            )
            "cappadocia" -> listOf(
                ItineraryDay(
                    1,
                    "Göreme Cave Valley Check-in",
                    "Arrive in Cappadocia and check into a stone-carved cave guesthouse.",
                    "Explore Göreme Open-Air Museum rock chapels and Uchisar Castle lookout.",
                    "Slow clay-pottery dinner watching dusk settle over the valleys.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Sunrise Balloons & Fairy Chimneys",
                    "Pre-dawn rooftop or Love Valley viewpoint as dozens of balloons lift into the sky.",
                    "Walk through Pasabag Monks Valley and Red Valley sandstone trails.",
                    "Stargazing from your cave terrace with warm salep.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Pottery Village & Farewell",
                    "Morning visit to Avanos riverside pottery workshops.",
                    "Underground city tour in Kaymaklı before evening transfer.",
                    "Relaxed farewell dinner before return journey $origin.",
                    perDayBudget
                )
            )
            "santorini" -> listOf(
                ItineraryDay(
                    1,
                    "Fira Cliffs & Caldera Views",
                    "Arrive and settle into whitewashed Cycladic rooms overlooking the Aegean.",
                    "Stroll Fira's cliffside marble lanes and blue-domed chapels.",
                    "Sunset dinner with tomato keftedes above the caldera.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Fira to Oia Scenic Walk",
                    "Walk the breezy caldera ridge trail from Fira through Imerovigli.",
                    "Cool off with Greek iced freddo espresso and local bakery pastries.",
                    "Golden hour in Oia followed by Ammoudi Bay harbor seafood.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Island Cove & Sail",
                    "Morning boat excursion past volcanic hot springs and Red Beach cliffs.",
                    "Swim in crystal turquoise coves and relax on deck.",
                    "Quiet lantern-lit taverna evening in Pyrgos hill village.",
                    perDayBudget
                ),
                ItineraryDay(
                    4,
                    "Aegean Morning & Departure",
                    "Slow Cycladic breakfast with figs, yogurt, and honey.",
                    "Last blue-dome souvenir stroll before heading to port or airport.",
                    "Return journey $origin.",
                    perDayBudget / 2
                )
            )
            "tunis" -> listOf(
                ItineraryDay(
                    1,
                    "Sidi Bou Said Blue & White",
                    "Morning stroll through jasmine-scented cobblestone lanes and cobalt doors.",
                    "Mint tea with pine nuts at Café des Délices overlooking the Gulf of Tunis.",
                    "Sunset walk along La Marsa corniche and grilled sea bream dinner.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Carthage & Tunis Medina",
                    "Explore Antonine Baths and Byrsa Hill ruins in Carthage.",
                    "Dive into Tunis Medina spice, copper, and chechia artisan souks.",
                    "Courtyard Tunisian brik & couscous dinner in Dar El Medina.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Bardo Mosaics & Coastal Relax",
                    "Admire Roman floor mosaics at the Bardo National Museum.",
                    "Afternoon espresso & pastries in Gammarth by the Mediterranean.",
                    "Wrap up your getaway with fresh fruit & seaside sunset.",
                    perDayBudget
                )
            )
            "petra" -> listOf(
                ItineraryDay(
                    1,
                    "Desert Road to Wadi Musa",
                    "Arrive via Amman and travel the scenic King's Highway to Wadi Musa.",
                    "Relax at a hillside lodge and sample traditional Jordanian mansaf.",
                    "Early rest for a sunrise start inside the rose-red canyon.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "The Siq & The Treasury",
                    "Enter the narrow Siq gorge as morning sun illuminates the Treasury facade.",
                    "Continue past the Street of Facades, Roman Theater, and Royal Tombs.",
                    "Optional candlelit Petra by Night walk under desert stars.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "The Monastery Climb",
                    "Hike 800 stone steps to Ad-Deir Monastery with panoramic desert valley views.",
                    "Sip Bedouin sage tea with canyon elders along the ridge.",
                    "Warm spiced dinner back in Wadi Musa.",
                    perDayBudget
                ),
                ItineraryDay(
                    4,
                    "Little Petra & Return",
                    "Morning detour to Little Petra's carved canyon caves.",
                    "Transfer back toward Amman airport with dates and Dead Sea salts.",
                    "Return flight $origin.",
                    perDayBudget / 2
                )
            )
            "marrakech" -> listOf(
                ItineraryDay(
                    1,
                    "Riad Oasis & Medina Lanterns",
                    "Arrive $origin and check into a tiled courtyard riad inside the Medina.",
                    "Mint tea on the rooftop terrace before wandering Souk Semmarine.",
                    "Jemaa el-Fnaa open-air spice stalls, tagines, and storytellers.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Palaces & Cobalt Gardens",
                    "Morning visit to Jardin Majorelle's vibrant blue villas and cacti.",
                    "Tour Bahia Palace's painted cedar ceilings and zellige courtyards.",
                    "Relaxing traditional hammam scrub followed by rooftop couscous.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Agafay Palms & Desert Sunset",
                    "Leisurely morning browsing brass lanterns and leather craft alleys.",
                    "Afternoon escape to Agafay stone desert dunes for golden-hour camel ride.",
                    "Stargazing tea and grilled skewers by campfire.",
                    perDayBudget
                ),
                ItineraryDay(
                    4,
                    "Atlas Morning & Departure",
                    "Orange juice and warm msemen pancakes in the riad courtyard.",
                    "Last spice & argan oil shopping before airport transfer.",
                    "Return flight $origin.",
                    perDayBudget / 2
                )
            )
            else -> listOf(
                ItineraryDay(
                    1,
                    "Old Town Walls & Stradun",
                    "Arrive $origin and walk through Pile Gate onto limestone Stradun.",
                    "Loop the medieval city walls for sweeping terracotta rooftop views.",
                    "Gelato and sunset drinks along Buža cliffside bar.",
                    perDayBudget
                ),
                ItineraryDay(
                    2,
                    "Lokrum Island Kayak",
                    "Morning paddle past sea caves to lush Lokrum Island botanical reserve.",
                    "Swim in crystal Adriatic coves and relax under pine shade.",
                    "Fresh black risotto dinner in the Old Port.",
                    perDayBudget
                ),
                ItineraryDay(
                    3,
                    "Mount Srđ Cable Car",
                    "Ride up to Mount Srđ ridge for full Dalmatian coast panoramas.",
                    "Explore hidden stone alleys and local artisan galleries.",
                    "Quiet sunset promenade overlooking Fort Lovrijenac.",
                    perDayBudget
                ),
                ItineraryDay(
                    4,
                    "Harbor Morning & Departure",
                    "Espresso by the Old Harbor boats.",
                    "Pick up lavender sachets and candied orange peel before transfer.",
                    "Return flight $origin.",
                    perDayBudget / 2
                )
            )
        }

        val packingTip = when (destination.primaryVibe) {
            TravelVibe.ISLAND -> "Pack reef-safe sunscreen, breezy linen layers, and grippy sandals for stone steps."
            TravelVibe.DESERT -> "Bring a light scarf for sun & breeze, layered cardigan for cool evenings, and comfy walking shoes."
            TravelVibe.HISTORY -> "Wear supportive walking sneakers, a wide-brim hat, and carry a refillable water bottle."
            else -> "Pack comfortable street sneakers, a compact tote for markets, and a light evening jacket."
        }

        return GeneratedTravelPlan(
            id = UUID.randomUUID().toString(),
            destinationId = destination.id,
            destinationName = destination.name,
            country = destination.country,
            origin = origin,
            budgetTnd = effectiveBudget,
            vibeLabel = activeVibeLabel,
            summary = "Sample $dayCount-day ${activeVibeLabel.lowercase()} getaway to ${destination.name} tailored around $origin with a balanced budget.",
            flightEstimateTnd = flightCost,
            stayEstimateTnd = stayCost,
            foodAndActivitiesTnd = foodCost,
            packingTip = packingTip,
            localTip = "${destination.flightTimeFromTunisia} • Best visited ${destination.bestSeason}.",
            days = days.take(dayCount),
            isSamplePreview = true
        )
    }
}
