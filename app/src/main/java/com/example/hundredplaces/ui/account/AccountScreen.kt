package com.example.hundredplaces.ui.account

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.components.LoadingScreen
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination
import com.example.hundredplaces.ui.theme.Typography

object AccountDestination : MenuNavigationDestination {
    override val route = "Account"
    override val title = R.string.account
    override val iconRes = R.drawable.baseline_account_circle_24
}

/**
 * Entry route for Account screen
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    accountViewModel: AccountViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
){
    val accountUiState = accountViewModel.uiState.collectAsStateWithLifecycle().value
    var showDialog by remember { mutableStateOf(false) }

    accountUiState.userMessage?.let { userMessage ->
        val snackbarText = stringResource(userMessage)
        LaunchedEffect(snackbarHostState, accountViewModel, userMessage, snackbarText) {
            snackbarHostState.showSnackbar(snackbarText)
            accountViewModel.snackbarMessageShown()
        }
    }

    if (showDialog) {
        UpdateAccountDialog(
            accountViewModel = accountViewModel,
            onDismissRequest = {
                showDialog = false
                accountViewModel.nameState.clearText()
                accountViewModel.emailState.clearText()
                accountViewModel.oldPasswordState.clearText()
                accountViewModel.newPasswordState.clearText()
                accountViewModel.repeatNewPasswordState.clearText()
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                OutlinedCard {
                    var expanded by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .animateContentSize()
                            .clickable(onClick = {
                                expanded = !expanded
                                accountViewModel.pullUser()
                            })
                            .padding(
                                horizontal = dimensionResource(R.dimen.padding_medium),
                                vertical = dimensionResource(R.dimen.padding_small)
                            )
                            .fillMaxWidth()
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceTint,
                                modifier = Modifier
                                    .padding(end = dimensionResource(id = R.dimen.padding_small))
                            )
                            Text(
                                text = stringResource(R.string.account),
                                style = Typography.titleLarge
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    expanded = !expanded
                                    accountViewModel.pullUser()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(if (expanded) R.drawable.rounded_keyboard_arrow_up_24 else R.drawable.rounded_keyboard_arrow_down_24),
                                    contentDescription = stringResource(if (expanded)R.string.expand_more else R.string.expand_less)
                                )
                            }
                        }
                        if (expanded) {
                            if (accountUiState.isLoading) {
                                LoadingScreen(
                                    modifier = Modifier
                                        .zIndex(0.1f)
                                        .padding(dimensionResource(R.dimen.padding_medium))
                                )
                            }
                            else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                                    modifier = Modifier
                                        .padding(
                                            start = 24.dp,
                                            bottom = dimensionResource(R.dimen.padding_medium)
                                        )
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                                    ) {
                                        Text(
                                            text = stringResource(R.string.name),
                                            style = Typography.titleMedium
                                        )
                                        Text(
                                            text = if (accountUiState.userName.isNotEmpty()) accountUiState.userName else "Unavailable",
                                            style = Typography.bodyLarge
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                                    ) {
                                        Text(
                                            text = stringResource(R.string.email),
                                            style = Typography.titleMedium
                                        )
                                        Text(
                                            text = if (accountUiState.userEmail.isNotEmpty()) accountUiState.userEmail else "Unavailable",
                                            style = Typography.bodyLarge
                                        )
                                    }
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        space = dimensionResource(R.dimen.padding_medium),
                                        alignment = Alignment.End),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = { showDialog = true }
                                    ) {
                                        Text(stringResource(R.string.edit))
                                    }
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.error
                                        ),
                                        onClick = { accountViewModel.logOut() }
                                    ) {
                                        Text(stringResource(R.string.log_out))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.credits),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}