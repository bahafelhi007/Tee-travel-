package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_destinations")
data class SavedDestinationEntity(
    @PrimaryKey val destinationId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_trips")
data class SavedTripEntity(
    @PrimaryKey val id: String,
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
    val daysJson: String,
    val packingTip: String,
    val localTip: String,
    val isSamplePreview: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
