package com.example.getaway

import com.russhwolf.settings.Settings

object TripStorage {
    private val settings: Settings = Settings()

    fun saveTripTitle(data: String) {
        settings.putString("trip_title", data)
    }
    fun loadTripTitle(): String {
        return settings.getString("trip_title", "")
    }

    fun savePersonCount(data: String) {
        settings.putString("person_count", data)
    }
    fun loadPersonCount(): String {
        return settings.getString("person_count", "")
    }
}