package com.example.hundredplaces.data.services.distance

import android.location.Location
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DistanceServiceImpl(
    placeRepository: PlaceRepository
) : DistanceService {

    private val _places = placeRepository.allPlaces
    private val _distances = MutableStateFlow<Map<Long, Float>>(emptyMap())
    override val distances: StateFlow<Map<Long, Float>> = _distances

    override fun getDistances(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            _places.collect { places->
                _distances.value = buildMap {
                    places.forEach { place->
                        val placeLocation = Location("").apply {
                            latitude = place.latitude
                            longitude = place.longitude
                        }
                        put(place.id, location.distanceTo(placeLocation))
                    }
                }
            }
        }
    }
}