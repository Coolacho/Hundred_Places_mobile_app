package com.example.hundredplaces.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.theme.Typography

@Composable
fun CreateAccountForm(
    navigateToLogin: () -> Unit,
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
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = stringResource(R.string.create_an_account),
                    style = Typography.titleLarge
                )
            }
            OutlinedTextField(
                state = loginViewModel.nameState,
                isError = loginViewModel.nameHasErrors,
                label = { Text(stringResource(R.string.name)) },
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                supportingText = {
                    Text("Password must be between 8 and 50 characters long and must contain an uppercase letter," +
                            "a lowercase letter, a number and a special character (#, ?, !, @, $, %, ^, &, *, -)")
                },
            )
            Row (
                horizontalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(R.dimen.padding_medium),
                    alignment = Alignment.End
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(OutlinedTextFieldDefaults.MinWidth)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = navigateToLogin
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    enabled = loginViewModel.canCreateAccount(),
                    onClick = loginViewModel::createUser
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    }
}