package com.example.hundredplaces.ui.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.navigation.NavigationDestination
import com.example.hundredplaces.ui.theme.HundredPlacesTheme

object AccountDestination : NavigationDestination {
    override val route = "Account"
    override val title = R.string.account
    override val iconRes = R.drawable.baseline_account_circle_24
}

/**
 * Entry route for Account screen
 */
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
){
    val uiState = viewModel.uiState.collectAsState()

    LazyColumn(
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
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
                            value = uiState.value.userDetails.name,
                            onValueChange = {viewModel.updateUiState(uiState.value.userDetails.copy(name = it))},
                            readOnly = true,
                            label = { Text(
                                text = stringResource(R.string.name),
                                fontSize = 16.sp
                            ) },
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                                .fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = uiState.value.userDetails.email,
                            onValueChange = {viewModel.updateUiState(uiState.value.userDetails.copy(email = it))},
                            readOnly = true,
                            label = { Text(
                                text = stringResource(R.string.email),
                                fontSize = 16.sp
                                ) },
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.padding_medium))
                                .fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = uiState.value.userDetails.password,
                            onValueChange = {viewModel.updateUiState(uiState.value.userDetails.copy(password = it))},
                            readOnly = true,
                            label = { Text(
                                text = stringResource(R.string.password),
                                fontSize = 16.sp
                            ) },
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
                                enabled = true,
                                onClick = { viewModel.saveUser() }
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
                                onClick = { viewModel.logOut() }
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

@Preview
@Composable
private fun AccountScreenPreview() {
    HundredPlacesTheme {
        Surface {
            AccountScreen()
        }
    }
}