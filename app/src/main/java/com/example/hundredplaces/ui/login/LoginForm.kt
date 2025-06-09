package com.example.hundredplaces.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.hundredplaces.R

@Composable
fun LoginForm(
    navigateToCreateAccount: () -> Unit,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.padding_medium),
                alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.log_in_or_create_an_account),
            )
            OutlinedTextField(
                state = loginViewModel.emailState,
                isError = loginViewModel.emailHasErrors,
                label = { Text(stringResource(R.string.email)) },
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
            OutlinedSecureTextField(
                state = loginViewModel.passwordState,
                isError = loginViewModel.passwordHasErrors,
                label = { Text(stringResource(R.string.password)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(OutlinedTextFieldDefaults.MinWidth)
            ) {
                Button(
                    enabled = loginViewModel.canLogIn(),
                    onClick = loginViewModel::logIn
                ) {
                    Text(stringResource(R.string.log_in))
                }
                Button(
                    onClick = navigateToCreateAccount
                ) {
                    Text(stringResource(R.string.create_an_account))
                }
            }
        }
    }
}