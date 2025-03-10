package com.example.hundredplaces.ui.placeDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceDetailsViewModel(
    placesRepository: PlacesRepository,
    private val visitsRepository: VisitsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val placeIdFlow: StateFlow<Long> = savedStateHandle.getStateFlow(
        key = PlaceDetailsDestination.PLACE_ID_ARG,
        initialValue = 0L
    )
    private val userId: Long = 1//savedStateHandle["CURRENT_USER"]!!

    private val _visits = placeIdFlow.flatMapLatest { placeId ->
        visitsRepository.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)
    }
    private val _place: Flow<PlaceWithCityAndImages?> = placeIdFlow.flatMapLatest { placeId ->
        placesRepository.getPlaceWithCityAndImages(placeId)
    }
    private val _descriptionText = _place.flatMapLatest { place ->
        place?.place?.descriptionPath?.let { path ->
            if (path.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    try {
                        val stringBuilder = StringBuilder()
                        val url = URL(path)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.connectTimeout = 3000
                        connection.readTimeout = 3000
                        connection.requestMethod = "GET"

                        connection.connect()

                        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                                reader.forEachLine { line ->
                                    stringBuilder.append(line)
                                } }
                        }
                        else MutableStateFlow(null)

                        MutableStateFlow(stringBuilder.toString())
                    }
                    catch (_: ConnectException) {
                        MutableStateFlow(null)
                    }
                    catch (_: SocketTimeoutException) {
                        MutableStateFlow(null)
                    }
                }
            }
            else MutableStateFlow(null)
        } ?: MutableStateFlow(null)
    }


    val uiState = combine(
        _descriptionText, _visits, _place
    ) { descriptionText, visits, place ->
        PlaceDetailsUiState(
            place = place,
            descriptionText = descriptionText,
            visits = visits
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlaceDetailsUiState()
        )

    fun addVisit() {
        viewModelScope.launch {
            visitsRepository.insertVisit(
                Visit(
                    userId = userId,
                    placeId = placeIdFlow.value
                )
            )
        }
    }

    fun setPlaceId(id: Long) {
        savedStateHandle[PlaceDetailsDestination.PLACE_ID_ARG] = id
        Log.d("SavedStateHandle Test", "$id")
        Log.d("SavedStateHandle Test", "${savedStateHandle[PlaceDetailsDestination.PLACE_ID_ARG] ?: 0L}" )
    }

    fun getPlaceId(): Long {
        return savedStateHandle[PlaceDetailsDestination.PLACE_ID_ARG] ?: 0
    }
}