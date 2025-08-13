package com.sunnyweather.android.logic.model


data class PlaceResponse(val status: String, val places: List<Place>)
data class Place(val name: String, val longitude: String, val latitude: String, val address: String )