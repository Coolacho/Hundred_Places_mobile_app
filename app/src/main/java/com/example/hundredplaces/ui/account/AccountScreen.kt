package com.example.hundredplaces.ui.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.MenuNavigationDestination

object AccountDestination : MenuNavigationDestination {
    override val route = "Account"
    override val title = R.string.account
    override val iconRes = R.drawable.baseline_account_circle_24
}

/**
 * Entry route for Account screen
 */
@Composable
fun AccountScreen(
    uiState: AccountUiState,
    viewModel: AccountViewModel,
    navigateToLogIn: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
){
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                OutlinedCard(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceTint,
                                modifier = Modifier
                                    .padding(end = dimensionResource(id = R.dimen.padding_small))
                            )
                            Text(
                                text = stringResource(id = R.string.account),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = uiState.userDetails.name,
                                onValueChange = {viewModel.updateUserDetails(uiState.userDetails.copy(name = it))},
                                label = { Text(
                                    text = stringResource(R.string.name),
                                    fontSize = 16.sp
                                ) },
                                singleLine = true,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = R.dimen.padding_medium))
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = uiState.userDetails.email,
                                onValueChange = {viewModel.updateUserDetails(uiState.userDetails.copy(email = it))},
                                label = { Text(
                                    text = stringResource(R.string.email),
                                    fontSize = 16.sp
                                ) },
                                singleLine = true,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = R.dimen.padding_medium))
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = uiState.userDetails.password,
                                onValueChange = {viewModel.updateUserDetails(uiState.userDetails.copy(password = it))},
                                label = { Text(
                                    text = stringResource(R.string.password),
                                    fontSize = 16.sp
                                ) },
                                singleLine = true,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = R.dimen.padding_medium))
                                    .fillMaxWidth()
                            )
                            Row(
                                modifier = Modifier
                                    .padding(
                                        top = dimensionResource(id = R.dimen.padding_medium)
                                    )
                            ) {
                                Button(
                                    shape = MaterialTheme.shapes.small,
                                    enabled = viewModel.validateInput(),
                                    onClick = { viewModel.updateUser() }
                                ) {
                                    Text(
                                        text = stringResource(R.string.save),
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    ),
                                    onClick = { viewModel.logOut()
                                        navigateToLogIn() }
                                ) {
                                    Text(
                                        text = stringResource(R.string.log_out),
                                        fontSize = 16.sp
                                    )
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