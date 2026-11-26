package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.TravelVibe
import com.example.model.TravelerProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.travelPrefsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "tee_travel_prefs"
)

class TravelPreferencesRepository(private val context: Context) {

    private object Keys {
        val TRAVELER_NAME = stringPreferencesKey("traveler_name")
        val HOME_ORIGIN = stringPreferencesKey("home_origin")
        val BUDGET_TND = intPreferencesKey("budget_tnd")
        val CURRENCY_CODE = stringPreferencesKey("currency_code")
        val PREFERRED_VIBE = stringPreferencesKey("preferred_vibe")
        val AVATAR_INDEX = intPreferencesKey("avatar_index")
    }

    val profileFlow: Flow<TravelerProfile> = context.travelPrefsDataStore.data.map { prefs ->
        val vibeName = prefs[Keys.PREFERRED_VIBE] ?: TravelVibe.ALL.name
        val vibe = TravelVibe.entries.firstOrNull { it.name == vibeName } ?: TravelVibe.ALL
        TravelerProfile(
            name = prefs[Keys.TRAVELER_NAME] ?: "Lina Ben Ali",
            homeOrigin = prefs[Keys.HOME_ORIGIN] ?: "From Tunisia",
            defaultBudgetTnd = prefs[Keys.BUDGET_TND] ?: 1500,
            currencyCode = prefs[Keys.CURRENCY_CODE] ?: "TND",
            preferredVibe = vibe,
            avatarColorIndex = prefs[Keys.AVATAR_INDEX] ?: 0
        )
    }

    suspend fun updateBudget(budgetTnd: Int) {
        context.travelPrefsDataStore.edit { it[Keys.BUDGET_TND] = budgetTnd }
    }

    suspend fun updateOrigin(origin: String) {
        context.travelPrefsDataStore.edit { it[Keys.HOME_ORIGIN] = origin }
    }

    suspend fun updateVibe(vibe: TravelVibe) {
        context.travelPrefsDataStore.edit { it[Keys.PREFERRED_VIBE] = vibe.name }
    }

    suspend fun updateCurrency(currencyCode: String) {
        context.travelPrefsDataStore.edit { it[Keys.CURRENCY_CODE] = currencyCode }
    }

    suspend fun updateName(name: String) {
        context.travelPrefsDataStore.edit { it[Keys.TRAVELER_NAME] = name.trim().ifEmpty { "Explorer" } }
    }

    suspend fun updateAvatarIndex(index: Int) {
        context.travelPrefsDataStore.edit { it[Keys.AVATAR_INDEX] = index }
    }
}
