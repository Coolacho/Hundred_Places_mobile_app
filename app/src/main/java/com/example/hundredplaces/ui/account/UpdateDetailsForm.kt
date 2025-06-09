package com.example.hundredplaces.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun UpdateDetailsForm(
    accountViewModel: AccountViewModel,
    onChangePassword: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(R.dimen.padding_medium),
            alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.rounded_edit_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            )
            Text(
                text = stringResource(R.string.edit_account),
                style = Typography.titleLarge
            )
        }
        OutlinedTextField(
            state = accountViewModel.nameState,
            isError = accountViewModel.nameHasErrors,
            label = { Text(stringResource(R.string.name)) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            state = accountViewModel.emailState,
            isError = accountViewModel.emailHasErrors,
            label = { Text(stringResource(R.string.email)) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(OutlinedTextFieldDefaults.MinWidth)
        ) {
            TextButton(
                onClick = onChangePassword
            ) {
                Text(stringResource(R.string.change_password))
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = onCancel
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_cancel_24),
                    contentDescription = stringResource(R.string.cancel)
                )
            }
            Button(
                enabled = accountViewModel.canUpdateAccount(),
                onClick = accountViewModel::updateUser
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}