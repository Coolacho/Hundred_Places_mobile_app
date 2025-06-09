package com.example.hundredplaces.ui.placeDetails

import android.content.Context
import android.icu.util.TimeZone
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.components.LoadingScreen
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.places.PlaceRating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object PlaceDetailsDestination : NavigationDestination {
    override val route = "Place Details"
    const val PLACE_ID_ARG = "placeId"
    const val ADD_VISIT_ARG = "addVisit"
    val routeWithArgs = "$route/{$PLACE_ID_ARG}?addVisit={$ADD_VISIT_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    navigateBack: () -> Unit,
    isFullScreen: Boolean,
    addVisit: Boolean,
    placesDetailsViewModel: PlaceDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = placesDetailsViewModel.uiState.collectAsStateWithLifecycle().value
    var isTTSInit by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val textToSpeech = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTTSInit = true
                Log.d("TTS", "TTS is initialized")
            }
        }
    }
    var isSpeaking by remember { mutableStateOf(false) }

    LaunchedEffect(isTTSInit) {
        if (isTTSInit) {
            val locale = configuration.locales.get(0)
            if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                textToSpeech.language = locale
            } else {
                textToSpeech.language = Locale.US
            }
            isTTSInit = false
        }
    }

    DisposableEffect(context) {

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    when(uiState) {
        is PlaceDetailsUiState.Error -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = stringResource(R.string.place_not_available)
                )
            }
        }
        is PlaceDetailsUiState.Loading -> {
            LoadingScreen(
                modifier = modifier
            )
        }
        is PlaceDetailsUiState.Success -> {

            var showDialog by remember { mutableStateOf(false) }
            var result by remember { mutableStateOf(false) }

            val tabs = listOf(R.string.information, R.string.visits)
            val pagerState = rememberPagerState(
                initialPage = tabs.indexOf(R.string.information),
                pageCount = { tabs.size }
            )
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(addVisit) {
                if (addVisit) {
                    result = placesDetailsViewModel.addVisit()
                    showDialog = true
                }
            }

            LaunchedEffect(uiState.place) {
                var text: String? = null
                if (uiState.place.place.descriptionPath.isNotEmpty()) {
                    while (true) {
                        text = getDescriptionText(context, uiState.place.place.descriptionPath)
                        if (text != null) break
                        delay(60_000)
                    }
                }
                placesDetailsViewModel.updateDescriptionText(text)
            }

            if (showDialog) {
                VisitDialog(
                    onDismissRequest = { showDialog = false },
                    onConfirmClick = {
                        showDialog = false
                        coroutineScope.launch { pagerState.animateScrollToPage(tabs.indexOf(R.string.visits)) }
                    },
                    result = result
                )
            }

            Scaffold(
                floatingActionButton = {
                    if (uiState.descriptionText != null) {
                        FloatingActionButton (
                            onClick = {
                                if (isSpeaking) {
                                    Log.d("TTS", "Stopped speaking")
                                    textToSpeech.stop()
                                    isSpeaking = !isSpeaking
                                } else {
                                    Log.d("TTS", "Started speaking")
                                    textToSpeech.speak(uiState.descriptionText, TextToSpeech.QUEUE_FLUSH, null, null)
                                    isSpeaking = !isSpeaking
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(if (isSpeaking) R.drawable.rounded_stop_circle_24 else R.drawable.headphones_24px),
                                contentDescription = "Description narration button"
                            )
                        }
                    }
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                modifier = modifier
            ) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    if (!isFullScreen) {
                        IconButton(
                            onClick = navigateBack,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .align(Alignment.TopStart)
                                .zIndex(0.1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    shape = CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.rounded_arrow_back_24),
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ImageCarousel(
                            images = uiState.place.images,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(225.dp)
                                .padding(
                                    bottom = dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                        if (!isFullScreen) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = uiState.place.place.name,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = uiState.place.city,
                                    )
                                }
                                PlaceRating(rating = uiState.place.place.rating)
                            }
                        }

                        PrimaryTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    text = {
                                        Text(
                                            text = stringResource(id = title),
                                            style = MaterialTheme.typography.labelLarge,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )
                                    },
                                    selected = pagerState.currentPage == index,
                                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                    }
                                )
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = 48.dp,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = dimensionResource(R.dimen.padding_small)
                                )
                        ) { page ->
                            when(page){
                                tabs.indexOf(R.string.information) -> InformationTab(uiState.descriptionText)
                                tabs.indexOf(R.string.visits) -> VisitsTab(uiState.visits)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VisitDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    result: Boolean,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            if (result) {
                TextButton(
                    onClick = onConfirmClick
                ) {
                    Text(
                        text = stringResource(R.string.view_visit)
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.dismiss)
                )
            }
        },
        title = {
            Text(
                text = if (result) stringResource(R.string.visit_added) else stringResource(R.string.failed_to_add_visit)
            )
        },
        icon = {
            Icon(
                painter = painterResource(if (result) R.drawable.rounded_check_circle_24 else R.drawable.rounded_cancel_24),
                contentDescription = null,
                tint = if (result) Color.Green.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.65f)
            )
        },
        text = {
            Text(
                text = if (result) stringResource(R.string.view_visit_in_visits_tab) else stringResource(R.string.add_visit_manually)
            )
        },
        modifier = modifier
    )
}

@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    if (images.isNotEmpty()) {
        Box(modifier = modifier) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { images.size }
            )
            HorizontalPager(
                state = pagerState,
                pageSpacing = 48.dp,
                modifier = Modifier
                    .clip(CardDefaults.shape)
            ) { page ->
                Card {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(images[page])
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.DarkGray
                    else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
    else {
        Image(
            painter = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null,
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CardDefaults.shape
                )
        )
    }
}

@Composable
fun InformationTab(
    descriptionText: String?,
    modifier: Modifier = Modifier
)
{
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
    ) {
        Text(
            text = descriptionText ?: stringResource(R.string.no_description_available),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .verticalScroll(scrollState)
        )
        AnimatedVisibility(
            visible = scrollState.canScrollForward,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // Adjust shadow height
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, MaterialTheme.colorScheme.background), // Shadow from transparent to background
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY // Fade effect
                        )
                    )
            )
        }
    }
}

@Composable
fun VisitsTab(
    visits: List<Instant>,
    modifier: Modifier = Modifier
)
{
    val timeZone = TimeZone.getDefault()
    val zoneId = ZoneId.of(timeZone.id)

    LazyColumn(
        modifier = modifier
    ) {
        if (visits.isEmpty())
        {
            item {
                Text(text = stringResource(R.string.place_not_visited))
            }
        }
        else
        {
            items(visits, key = { visits.indexOf(it) }) {
                val localDateTime = LocalDateTime.ofInstant(it, zoneId)
                Text(
                    text = "${localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}",
                    modifier = Modifier
                        .padding(bottom = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

private fun isCacheExpired(file: File): Boolean {
    if (!file.exists()) return true

    return System.currentTimeMillis() - file.lastModified() > 24 * 60 * 60 * 1000L // 24 hours
}

private fun getCachedDescriptionFile(context: Context, path: String): File {
    val fileName = path.hashCode().toString() + ".txt" // Unique name based on URL
    return File(context.cacheDir, fileName)
}

private suspend fun getDescriptionText(context: Context, path: String): String? {
    return withContext(Dispatchers.IO) {
        val cachedFile = getCachedDescriptionFile(context, path)

        if (cachedFile.exists() && !isCacheExpired(cachedFile)) {
            cachedFile.bufferedReader().use { return@withContext it.readText() }
        }

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

                cachedFile.writeText(stringBuilder.toString())
                cachedFile.setReadOnly()

                return@withContext stringBuilder.toString()
            }
            else return@withContext null

        }
        catch (_: ConnectException) {
            return@withContext null
        }
        catch (_: SocketTimeoutException) {
            return@withContext null
        }
    }
}