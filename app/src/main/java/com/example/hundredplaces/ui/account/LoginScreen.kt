package com.example.hundredplaces.ui.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.navigation.NavigationDestination

object LoginDestination : NavigationDestination {
    override val route = "Login"
    override val title: Int
    get() = TODO("Not yet implemented")
    override val iconRes: Int
    get() = TODO("Not yet implemented")
}

@Composable
fun LoginScreen(
    uiState: AccountUiState,
    viewModel: AccountViewModel,
    navigateToHome: () -> Unit,
    navigateToCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxSize()
        ) {
            item {
                OutlinedTextField(
                    value = uiState.userDetails.email,
                    onValueChange = {viewModel.updateUiState(uiState.userDetails.copy(email = it))},
                    label = { Text(
                        text = stringResource(R.string.email),
                        fontSize = 16.sp
                    ) },
                    singleLine = true,
                    isError = !uiState.isLoginSuccessful,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.padding_medium))
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.userDetails.password,
                    onValueChange = {viewModel.updateUiState(uiState.userDetails.copy(password = it))},
                    label = { Text(
                        text = stringResource(R.string.password),
                        fontSize = 16.sp
                    ) },
                    singleLine = true,
                    isError = !uiState.isLoginSuccessful,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.padding_medium))
                        .fillMaxWidth()
                )
                AnimatedVisibility(visible = !uiState.isLoginSuccessful) {
                    Text(
                        text = "Wrong email/password.\nPlease try again!",
                        color = Color.Red,
                        modifier = Modifier
                            .padding(top = dimensionResource(id = R.dimen.padding_medium))
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_medium)
                        )
                ) {
                    Button(
                        shape = MaterialTheme.shapes.small,
                        enabled = uiState.isEntryValid,
                        onClick = { if (viewModel.logIn()) {navigateToHome()} }
                    ) {
                        Text(
                            text = stringResource(R.string.log_in),
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        shape = MaterialTheme.shapes.small,
                        onClick = { navigateToCreateAccount() }
                    ) {
                        Text(
                            text = stringResource(R.string.create_an_account),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}