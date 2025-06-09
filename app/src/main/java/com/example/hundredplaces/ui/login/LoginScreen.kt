package com.example.hundredplaces.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.components.LoadingScreen
import com.example.hundredplaces.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay

object LoginDestination : NavigationDestination {
    override val route = "Login"
}

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    loginViewModel: LoginViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = loginViewModel.uiState.collectAsStateWithLifecycle().value

    // Animate rotationY between 0f and 180f
    val rotationYAnim by animateFloatAsState(
        targetValue = if (uiState.isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = LinearEasing),
        label = "flipAnimation"
    )

    val infinitePagesCount = 100
    val startIndex = infinitePagesCount / 2
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { infinitePagesCount },
    )
    LaunchedEffect(Unit) {
        while (true) {
            delay(4_000)
            pagerState.animateScrollToPage(
                page = pagerState.currentPage + 1,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearEasing
                )
            )
        }
    }

    uiState.userId?.let {
        LaunchedEffect(it) {
            navigateToHome()
        }
    }

    uiState.userMessage?.let { userMessage ->
        val snackbarText = stringResource(userMessage)
        LaunchedEffect(snackbarHostState, loginViewModel, userMessage, snackbarHostState) {
            snackbarHostState.showSnackbar(snackbarText)
            loginViewModel.snackbarMessageShown()
        }
    }

    if (uiState.isLoading) {
        LoadingScreen(
            alpha = 0.65f,
            modifier = Modifier
                .zIndex(0.2f)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
            .fillMaxSize()
    ) {
        if (uiState.images.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                val imageIndex = page % uiState.images.size
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(uiState.images[imageIndex])
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alpha = 0.75f,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    // Left shadow
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(12.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .align(Alignment.CenterStart)
                    )
                    // Right shadow
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(12.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.4f)
                                    )
                                )
                            )
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .zIndex(0.1f)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            cameraDistance = 12f * density
                            rotationY = rotationYAnim
                        }
                ) {
                    AnimatedContent(
                        targetState = uiState.isFlipped,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(
                                durationMillis = 400,
                                delayMillis = 400,
                                easing = LinearEasing)) togetherWith
                            fadeOut(animationSpec = tween(
                                durationMillis = 400,
                                easing = LinearEasing))
                        }
                    ) { targetState ->
                        if (!targetState) {
                            LoginForm(
                                navigateToCreateAccount = loginViewModel::changeForm,
                                loginViewModel = loginViewModel
                            )
                        } else {
                            CreateAccountForm(
                                navigateToLogin = loginViewModel::changeForm,
                                loginViewModel = loginViewModel,
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationY = 180f
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}