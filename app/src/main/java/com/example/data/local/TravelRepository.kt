package com.example.data.local

import com.example.model.GeneratedTravelPlan
import com.example.model.ItineraryDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

class TravelRepository(private val dao: TravelDao) {

    val savedDestinationIds: Flow<Set<String>> =
        dao.observeSavedDestinations().map { list -> list.map { it.destinationId }.toSet() }

    val savedTrips: Flow<List<GeneratedTravelPlan>> =
        dao.observeSavedTrips().map { list -> list.map { it.toDomain() } }

    suspend fun toggleSavedDestination(destinationId: String, currentlySaved: Boolean) {
        if (currentlySaved) {
            dao.removeSavedDestination(destinationId)
        } else {
            dao.saveDestination(SavedDestinationEntity(destinationId = destinationId))
        }
    }

    suspend fun saveTrip(plan: GeneratedTravelPlan) {
        dao.insertTrip(plan.toEntity())
    }

    suspend fun deleteTrip(tripId: String) {
        dao.deleteTripById(tripId)
    }

    private fun GeneratedTravelPlan.toEntity(): SavedTripEntity {
        val array = JSONArray()
        days.forEach { day ->
            val obj = JSONObject().apply {
                put("dayNumber", day.dayNumber)
                put("title", day.title)
                put("morning", day.morning)
                put("afternoon", day.afternoon)
                put("evening", day.evening)
                put("estimatedCostTnd", day.estimatedCostTnd)
            }
            array.put(obj)
        }
        return SavedTripEntity(
            id = id,
            destinationId = destinationId,
            destinationName = destinationName,
            country = country,
            origin = origin,
            budgetTnd = budgetTnd,
            vibeLabel = vibeLabel,
            summary = summary,
            flightEstimateTnd = flightEstimateTnd,
            stayEstimateTnd = stayEstimateTnd,
            foodAndActivitiesTnd = foodAndActivitiesTnd,
            daysJson = array.toString(),
            packingTip = packingTip,
            localTip = localTip,
            isSamplePreview = isSamplePreview
        )
    }

    private fun SavedTripEntity.toDomain(): GeneratedTravelPlan {
        val parsedDays = mutableListOf<ItineraryDay>()
        try {
            val array = JSONArray(daysJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                parsedDays.add(
                    ItineraryDay(
                        dayNumber = obj.optInt("dayNumber", i + 1),
                        title = obj.optString("title", "Explore"),
                        morning = obj.optString("morning", ""),
                        afternoon = obj.optString("afternoon", ""),
                        evening = obj.optString("evening", ""),
                        estimatedCostTnd = obj.optInt("estimatedCostTnd", 150)
                    )
                )
            }
        } catch (_: Exception) {
            // Ignore malformed rows gracefully
        }
        return GeneratedTravelPlan(
            id = id,
            destinationId = destinationId,
            destinationName = destinationName,
            country = country,
            origin = origin,
            budgetTnd = budgetTnd,
            vibeLabel = vibeLabel,
            summary = summary,
            flightEstimateTnd = flightEstimateTnd,
            stayEstimateTnd = stayEstimateTnd,
            foodAndActivitiesTnd = foodAndActivitiesTnd,
            packingTip = packingTip,
            localTip = localTip,
            days = parsedDays,
            isSamplePreview = isSamplePreview
        )
    }
}
