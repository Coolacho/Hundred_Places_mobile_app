package com.example.hundredplaces.ui.placeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.user.repositories.UsersRepository
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    usersRepository: UsersRepository,
    private val visitsRepository: VisitsRepository,
    private val _placeId: Long
) : ViewModel() {

    private val _userId = usersRepository.userId

    private val _visits = _userId.flatMapLatest { userId ->
        userId?.let { visitsRepository.getAllVisitDatesByUserIdAndPlaceId(userId, _placeId) } ?: flowOf(emptyList())
    }
    private val _place: Flow<PlaceWithCityAndImages?> = placesRepository.getPlaceWithCityAndImages(_placeId)
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
                    userId = _userId.value!!,
                    placeId = _placeId
                )
            )
        }
    }

}