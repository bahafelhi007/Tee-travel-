package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {

    @Query("SELECT * FROM saved_destinations ORDER BY savedAt DESC")
    fun observeSavedDestinations(): Flow<List<SavedDestinationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDestination(entity: SavedDestinationEntity)

    @Query("DELETE FROM saved_destinations WHERE destinationId = :destinationId")
    suspend fun removeSavedDestination(destinationId: String)

    @Query("SELECT * FROM saved_trips ORDER BY createdAt DESC")
    fun observeSavedTrips(): Flow<List<SavedTripEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: SavedTripEntity)

    @Query("DELETE FROM saved_trips WHERE id = :tripId")
    suspend fun deleteTripById(tripId: String)
}
