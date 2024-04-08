package com.example.hundredplaces.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.hundredplaces.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HundredPlacesTopBar(
    title: String,
    uiState: AppContentUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    canChangeLayout: Boolean,
    selectLayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (canChangeLayout) {
                IconButton(onClick = selectLayout) {
                    Icon(
                        painter = painterResource(id = uiState.toggleIcon),
                        contentDescription = stringResource(id = uiState.toggleContentDescription)
                    )
                }
            }
        },
        modifier = modifier
    )

}