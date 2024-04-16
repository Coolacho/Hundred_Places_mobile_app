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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.hundredplaces.R
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
    modifier: Modifier = Modifier
){
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
                            value = "Example",
                            onValueChange = {},
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
                            value = "Example",
                            onValueChange = {},
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
                            value = "Example",
                            onValueChange = {},
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
                                onClick = { /*TODO*/ }
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
                                onClick = { /*TODO*/ }
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
            OutlinedCard(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier
                                .padding(end = dimensionResource(id = R.dimen.padding_small))
                        )
                        Text(
                            text = stringResource(R.string.settings),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Column {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = stringResource(R.string.notifications),
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(
                                checked = true,
                                onCheckedChange = {},
                            )
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